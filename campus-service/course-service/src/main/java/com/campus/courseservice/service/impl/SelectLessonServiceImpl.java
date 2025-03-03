package com.campus.courseservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.campus.common.constants.CourseConstants;
import com.campus.common.constants.SelectLessonConstants;
import com.campus.courseservice.service.ICourseService;
import com.campus.courseservice.service.ILessonTimeService;
import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.common.enums.AppHttpCodeEnum;
import com.campus.model.course.pojos.Lesson;
import com.campus.model.course.pojos.LessonAndTime;
import com.campus.model.course.pojos.LessonTime;
import com.campus.model.selectLesson.dtos.SetTimeDto;
import com.campus.model.selectLesson.pojos.SelectLesson;
import com.campus.courseservice.mapper.SelectLessonMapper;
import com.campus.courseservice.service.ISelectLessonService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.utils.thread.UserThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static sun.nio.ch.IOStatus.check;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-02-01
 */
@Service
@Primary
@Slf4j
public class SelectLessonServiceImpl extends ServiceImpl<SelectLessonMapper, SelectLesson> implements ISelectLessonService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SelectLessonMapper selectLessonMapper;
    @Autowired
    private ILessonTimeService lessonTimeService;
    @Autowired
    private ICourseService courseService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RedissonClient redissonClient;

    private static final DefaultRedisScript<Long> CHECK_LESSON_SCRIPT;
    private static final DefaultRedisScript<Long> BACK_LESSON_SCRIPT;
    private ISelectLessonService proxy = null;

    static {
        CHECK_LESSON_SCRIPT = new DefaultRedisScript<>();
        CHECK_LESSON_SCRIPT.setLocation(new ClassPathResource("checkLesson.lua"));
        CHECK_LESSON_SCRIPT.setResultType(Long.class);
        BACK_LESSON_SCRIPT = new DefaultRedisScript<>();
        BACK_LESSON_SCRIPT.setLocation(new ClassPathResource("backLesson.lua"));
        BACK_LESSON_SCRIPT.setResultType(Long.class);
    }

    @Override
    public ResponseResult selectLesson(Long lessonId) {
        proxy = (ISelectLessonService) AopContext.currentProxy();
        String timeJson = stringRedisTemplate.opsForValue().get(SelectLessonConstants.SELECT_LESSON_TIME);
        if (timeJson == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "未设置选课时间");
        }
        SetTimeDto setTimeDto = JSON.parseObject(timeJson, SetTimeDto.class);
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        if (now.isBefore(setTimeDto.getBeginTime()) || now.isAfter(setTimeDto.getEndTime())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "未到选课时间");
        }

        ResponseResult selectable = courseService.check(lessonId);
        //以及课容量是否满了
        if (selectable.getCode() != AppHttpCodeEnum.SUCCESS.getCode()) {
            return selectable;
        }
        LessonAndTime lessonAndTime = JSON.parseObject(JSON.toJSONString(selectable.getData()), LessonAndTime.class);
        Long luaRes = stringRedisTemplate.execute(
                CHECK_LESSON_SCRIPT,
                Arrays.asList(
                        CourseConstants.LESSON_SELECTED + lessonAndTime.getId(),
                        CourseConstants.COURSE_SELECT_STU + lessonAndTime.getCourseId()
                ),
                lessonAndTime.getCapacity().toString(), UserThreadLocalUtil.getUser().toString()

        );
        int r = luaRes.intValue();
        if (r == 1) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "课程人数已满");
        } else if (r == 2) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "不能重复选课");
        }

        SelectLesson selectLesson = new SelectLesson();
        List<String> lessonTimes = new ArrayList<>();
        for (LessonTime lessonTime : lessonAndTime.getLessonTimes()) {
            String jsonString = JSON.toJSONString(lessonTime);
            stringRedisTemplate.opsForSet().add(CourseConstants.Lesson_Time_Cache,
                    jsonString);
            lessonTimes.add(jsonString);
        }
        selectLesson.setLessonTimes(lessonTimes);
        selectLesson.setLessonId(lessonId);
        selectLesson.setStudentId(UserThreadLocalUtil.getUser());
        selectLesson.setCapacity(lessonAndTime.getCapacity());
        selectLesson.setOption(1);
        selectLesson.setUuid(UUID.randomUUID().toString());
        stringRedisTemplate.opsForZSet().add(
                SelectLessonConstants.SELECT_DISTRIBUTED_LOCK+selectLesson.getLessonId()+":"+selectLesson.getStudentId(),
                selectLesson.getUuid(),System.currentTimeMillis());
        rabbitTemplate.convertAndSend(CourseConstants.SELECT_LESSON_EXCHANGE, CourseConstants.SELECT_LESSON_ROUTINGKEY,
                selectLesson);
        String selectNum = stringRedisTemplate.opsForValue().get(CourseConstants.LESSON_SELECTED + lessonAndTime.getId());
        Integer selectNumInteger = JSON.parseObject(selectNum, Integer.class);
        return ResponseResult.okResult(selectNumInteger);
    }

    public void handleSelectLesson(SelectLesson selectLesson) {
        log.info("lock");
        RLock lock =null;
        while(true) {
            String uid = stringRedisTemplate.opsForZSet().range(SelectLessonConstants.SELECT_DISTRIBUTED_LOCK
                            +selectLesson.getLessonId()+":"+selectLesson.getStudentId(), 0,
                    0).stream().findFirst().orElse(null);
            lock = redissonClient.getLock(SelectLessonConstants.SELECT_LESSON_LOCK + selectLesson.getLessonId() + ":"
                    + selectLesson.getStudentId() + uid);
            lock.lock();
            if (!uid.equals(selectLesson.getUuid())) {
                lock.unlock();
            }else{
                break;
            }
        }
        try {
            if (proxy != null){
                if(selectLesson.getOption()==1){
                    proxy.selectLessonAndSave(selectLesson);
                }else{
                    proxy.backLessonAndSave(selectLesson);
                }
            }

            else {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stringRedisTemplate.opsForZSet().remove(SelectLessonConstants.SELECT_DISTRIBUTED_LOCK
                    +selectLesson.getLessonId()+":"+selectLesson.getStudentId(),
                    selectLesson.getUuid());
            lock.unlock();
        }

    }


    @Override
    public ResponseResult backLesson(Long lessonId) {
        String timeJson = stringRedisTemplate.opsForValue().get(SelectLessonConstants.SELECT_LESSON_TIME);
        if (timeJson == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "未设置选课时间");
        }
        SetTimeDto setTimeDto = JSON.parseObject(timeJson, SetTimeDto.class);
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        log.info("now:{}", now);
        if (now.isBefore(setTimeDto.getBeginTime()) || now.isAfter(setTimeDto.getEndTime())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "未到选课时间");
        }
        proxy = (ISelectLessonService) AopContext.currentProxy();
        ResponseResult backResult = courseService.backLessonCheck(lessonId);
        if (!backResult.getCode().equals(AppHttpCodeEnum.SUCCESS.getCode())) {
            return backResult;
        }
        Lesson lesson = JSON.parseObject(JSON.toJSONString(backResult.getData()), Lesson.class);
        Long luaRes = stringRedisTemplate.execute(
                BACK_LESSON_SCRIPT,
                Arrays.asList(
                        CourseConstants.LESSON_SELECTED + lesson.getId(),
                        CourseConstants.COURSE_SELECT_STU + lesson.getCourseId()
                ),
                UserThreadLocalUtil.getUser().toString()
        );
        int r = luaRes.intValue();
        if (r == 1) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "当前选课人数为0");
        } else if (r == 2) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "该学生未选这门课");
        }
        SelectLesson selectLesson = new SelectLesson();
        selectLesson.setLessonId(lesson.getId());
        selectLesson.setStudentId(UserThreadLocalUtil.getUser());
        selectLesson.setOption(0);
        selectLesson.setUuid(UUID.randomUUID().toString());
        stringRedisTemplate.opsForZSet().add(
                SelectLessonConstants.SELECT_DISTRIBUTED_LOCK+selectLesson.getLessonId()+":"+selectLesson.getStudentId(),
                selectLesson.getUuid(),System.currentTimeMillis());
        rabbitTemplate.convertAndSend(CourseConstants.SELECT_LESSON_EXCHANGE, CourseConstants.SELECT_LESSON_ROUTINGKEY,
                selectLesson);
        String selectNum =
                stringRedisTemplate.opsForValue().get(CourseConstants.LESSON_SELECTED + lesson.getId());
        Integer selectNumInteger = JSON.parseObject(selectNum, Integer.class);
        return ResponseResult.okResult(selectNumInteger);

    }

    @Override
    @Transactional
    public void selectLessonAndSave(SelectLesson selectLesson) {
        //一人只能选一次课
        Long count = lambdaQuery().eq(SelectLesson::getLessonId, selectLesson.getLessonId())
                .eq(SelectLesson::getStudentId, selectLesson.getStudentId()).count();
        if (count > 0) {
            log.info("不允许重复选课");
            return;
        }
        boolean success = Db.lambdaUpdate(Lesson.class)
                .setSql("selected=selected+1")
                .eq(Lesson::getId, selectLesson.getLessonId())
                .lt(Lesson::getSelected, selectLesson.getCapacity())
                .update();
        if (!success) {
            log.info("课容量已满");
            return;
        }
        save(selectLesson);
        for (String lessonTime : selectLesson.getLessonTimes()) {
            stringRedisTemplate.opsForSet().remove(CourseConstants.Lesson_Time_Cache, lessonTime);
        }

    }

    @Override
    @Transactional
    public void backLessonAndSave(SelectLesson selectLesson) {
        Long count = lambdaQuery().eq(SelectLesson::getLessonId, selectLesson.getLessonId())
                .eq(SelectLesson::getStudentId, selectLesson.getStudentId()).count();
        if (count <= 0) {
            log.info("未选这门课");
            return;
        }
        boolean success = Db.lambdaUpdate(Lesson.class)
                .setSql("selected=selected-1")
                .eq(Lesson::getId, selectLesson.getLessonId())
                .gt(Lesson::getSelected, 0)
                .update();
        if (!success) {
            log.info("选课人数为0");
            return;
        }
        remove(new LambdaQueryWrapper<SelectLesson>()
                .eq(SelectLesson::getLessonId, selectLesson.getLessonId())
                        .eq(SelectLesson::getStudentId, selectLesson.getStudentId())
                );

    }

    @Override
    public ResponseResult setTime(SetTimeDto setTimeDto) {
        if (setTimeDto == null || setTimeDto.getBeginTime() == null || setTimeDto.getEndTime() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        stringRedisTemplate.opsForValue().set(SelectLessonConstants.SELECT_LESSON_TIME,
                JSON.toJSONString(setTimeDto));
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult getTime() {
        String startTime = stringRedisTemplate.opsForValue().get(SelectLessonConstants.SELECT_LESSON_TIME);
        if (startTime == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "未设置抢课时间");
        }
        return ResponseResult.okResult(JSON.parseObject(startTime, SetTimeDto.class));
    }

    //查询学生课程表
    @Override
    public ResponseResult listByStuId() {
        Long user = UserThreadLocalUtil.getUser();
        List<Long> lessonIds = selectLessonMapper.getByStuId(user, CourseConstants.getTerm());
        List<Long> rest = new ArrayList<>();
        List<LessonTime> result = new ArrayList<>();
        for (Long id : lessonIds) {
            String lessonJson =
                    stringRedisTemplate.opsForValue().get(CourseConstants.LESSON_TIME + id);
            if (lessonJson == null) {
                rest.add(id);
            } else {
                List<LessonTime> times = JSON.parseArray(lessonJson, LessonTime.class);
                result.addAll(times);
            }
        }
        if (!rest.isEmpty()) {
            ResponseResult clientResult = lessonTimeService.getLessonTimes(rest);
            if (clientResult.getCode() != AppHttpCodeEnum.SUCCESS.getCode()) {
                return clientResult;
            }
            String jsonString = JSON.toJSONString(clientResult.getData());
            List<LessonTime> lessonTimes = JSON.parseArray(jsonString, LessonTime.class);

            if (lessonTimes != null) {
                result.addAll(lessonTimes);
            }
        }
//        result= result.stream().sorted(Comparator.comparing(LessonTime::getDayOfWeek).thenComparing(LessonTime::getBeginTime)
//        .thenComparing(LessonTime::getEndTime)).collect(Collectors.toList());
        return ResponseResult.okResult(result);
    }


}

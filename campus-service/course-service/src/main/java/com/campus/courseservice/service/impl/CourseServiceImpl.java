package com.campus.courseservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.campus.common.constants.CourseConstants;
import com.campus.common.util.CacheService;
import com.campus.courseservice.service.IMajorCourseService;
import com.campus.courseservice.service.IPreCourseService;
import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.common.enums.AppHttpCodeEnum;
import com.campus.model.course.dtos.CourseApplyDto;
import com.campus.model.course.pojos.Course;
import com.campus.courseservice.mapper.CourseMapper;
import com.campus.courseservice.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.model.course.pojos.MajorCourse;
import com.campus.model.course.pojos.PreCourse;
import com.campus.model.course.vo.CourseListVo;
import com.campus.model.teacher.dtos.TeacherCourseDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2025-01-19
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {
    @Autowired
    private CacheService cacheService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private IPreCourseService preCourseService;
    @Autowired
    private IMajorCourseService majorCourseService;

    private static final DefaultRedisScript<String> APPLY_COURSE_SCRIPT ;

    static {
        APPLY_COURSE_SCRIPT = new DefaultRedisScript<>();
        APPLY_COURSE_SCRIPT.setLocation(new ClassPathResource("applyCourse.lua"));
        APPLY_COURSE_SCRIPT.setResultType(String.class);
    }
    @Override
    public ResponseResult checkAddCourse() {
        Set<String> scan = cacheService.scan(CourseConstants.ADD_COURSE_TOPIC + "*");
        List<TeacherCourseDto> dtoList=new ArrayList<>();
        for (String key : scan) {
            String value = cacheService.get(key);
            TeacherCourseDto dto = JSON.parseObject(value, TeacherCourseDto.class);
            dtoList.add(dto);
        }
        return ResponseResult.okResult(dtoList);
    }

    @Override
    public ResponseResult checkCourse(Long id) {
        Course course = lambdaQuery().eq(Course::getId, id).one();
        if (course == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }else{
            return ResponseResult.okResult(course);
        }
    }

    @Override
    public ResponseResult listAll(Course course) {
        List<CourseListVo> courseListVoList = lambdaQuery().eq(course.getId() != null, Course::getId, course.getId())
                .eq(course.getCredit() != null, Course::getCredit, course.getCredit())
                .like(course.getName() != null, Course::getName, "%" + course.getName() + "%")
                .list().stream().map(courses -> {
                    CourseListVo courseListVo = new CourseListVo();
                    BeanUtils.copyProperties(courses, courseListVo);
                    courseListVo.setCredit(courses.getCredit()/10.0);
                    return courseListVo;
                }).collect(Collectors.toList());
        return ResponseResult.okResult(courseListVoList);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult applyCourse(CourseApplyDto courseApplyDto) {
        String result = stringRedisTemplate.execute(
                APPLY_COURSE_SCRIPT,
                Collections.emptyList(),
                CourseConstants.ADD_COURSE_TOPIC + courseApplyDto.getUid()
        );
        if(result==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        if(!courseApplyDto.getApply().equals(1)){
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        TeacherCourseDto dto = JSON.parseObject(result, TeacherCourseDto.class);
        Course course = new Course();
        course.setCredit((int) (dto.getCredit()*10));
        course.setName(dto.getName());
        save(course);
        for (Integer preId : dto.getPreIds()) {
            PreCourse preCourse = new PreCourse();
            preCourse.setAfterId(course.getId());
            preCourse.setPreId(preId.longValue());
            preCourseService.save(preCourse);
        }
        for (MajorCourse majorCourse : dto.getMajors()) {
            majorCourse.setCourseId(course.getId());
            majorCourseService.save(majorCourse);
        }
        return ResponseResult.okResult(course.getId());
    }

    @Override
    public ResponseResult getCourseByMajors(List<Long> majors) {
        if(majors==null||majors.isEmpty()){
            return ResponseResult.okResult(null);
        }
        List<Long> courseIds = Db.lambdaQuery(MajorCourse.class).in(MajorCourse::getMajorId, majors)
                .list().stream().map(MajorCourse::getCourseId).distinct().collect(Collectors.toList());
        if(courseIds==null||courseIds.isEmpty()){
            return ResponseResult.okResult(null);
        }
        List<CourseListVo> courseListVoList = lambdaQuery().in(Course::getId, courseIds).list().stream().map(courses -> {
            CourseListVo courseListVo = new CourseListVo();
            BeanUtils.copyProperties(courses, courseListVo);
            courseListVo.setCredit(courses.getCredit() / 10.0);
            return courseListVo;
        }).collect(Collectors.toList());

        return ResponseResult.okResult(courseListVoList);
    }


}

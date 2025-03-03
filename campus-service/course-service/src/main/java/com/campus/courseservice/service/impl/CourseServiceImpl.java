package com.campus.courseservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.campus.apis.user.IUserClient;
import com.campus.common.constants.CourseConstants;
import com.campus.common.constants.UserConstants;
import com.campus.common.util.CacheService;
import com.campus.courseservice.mapper.LessonMapper;
import com.campus.courseservice.mapper.SelectLessonMapper;
import com.campus.courseservice.service.IMajorCourseService;
import com.campus.courseservice.service.IPreCourseService;
import com.campus.courseservice.service.ISelectLessonService;
import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.common.enums.AppHttpCodeEnum;
import com.campus.model.course.dtos.CourseApplyDto;
import com.campus.model.course.dtos.CourseListTreeDto;
import com.campus.model.course.pojos.*;
import com.campus.courseservice.mapper.CourseMapper;
import com.campus.courseservice.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.model.course.vo.CourseListTreeVo;
import com.campus.model.course.vo.CourseListVo;
import com.campus.model.selectLesson.pojos.SelectLesson;
import com.campus.model.teacher.dtos.TeacherCourseDto;
import com.campus.model.user.pojos.Students;
import com.campus.utils.common.RedisExpireUtil;
import com.campus.utils.thread.UserThreadLocalUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
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
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private IUserClient userClient;
    @Autowired
    private SelectLessonMapper selectLessonMapper;
    @Autowired
    private LessonMapper lessonMapper;
    @Autowired
    private ISelectLessonService selectLessonService;


    private static final DefaultRedisScript<String> APPLY_COURSE_SCRIPT;

    static {
        APPLY_COURSE_SCRIPT = new DefaultRedisScript<>();
        APPLY_COURSE_SCRIPT.setLocation(new ClassPathResource("applyCourse.lua"));
        APPLY_COURSE_SCRIPT.setResultType(String.class);
    }

    @Override
    public ResponseResult checkAddCourse() {
        Set<String> scan = cacheService.scan(CourseConstants.ADD_COURSE_TOPIC + "*");
        List<TeacherCourseDto> dtoList = new ArrayList<>();
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
        } else {
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
                    courseListVo.setCredit(courses.getCredit() / 10.0);
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
        if (result == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        if (!courseApplyDto.getApply().equals(1)) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        TeacherCourseDto dto = JSON.parseObject(result, TeacherCourseDto.class);
        Course course = new Course();
        course.setCredit((int) (dto.getCredit() * 10));
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
            stringRedisTemplate.delete(CourseConstants.COURSE_MAJOR_IN_THIS_TERM+majorCourse.getMajorId());
        }
        stringRedisTemplate.delete(CourseConstants.COURSE_ALL_IN_THIS_TERM);
        return ResponseResult.okResult(course.getId());
    }

    @Override
    public ResponseResult getCourseByMajors(List<Long> majors) {
        if (majors == null || majors.isEmpty()) {
            return ResponseResult.okResult(null);
        }
        List<Long> courseIds = Db.lambdaQuery(MajorCourse.class).in(MajorCourse::getMajorId, majors)
                .list().stream().map(MajorCourse::getCourseId).distinct().collect(Collectors.toList());
        if (courseIds == null || courseIds.isEmpty()) {
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


    @Override
    public ResponseResult treeListInSchema(CourseListTreeDto dto) {
        dto.checkParam();
        //获取学生信息
        Students user = getUserInfo(UserThreadLocalUtil.getUser());

        //获取所有课程
        String courses = cacheService.get(CourseConstants.COURSE_MAJOR_IN_THIS_TERM + user.getMajor());
        List<Course> courseList = null;
        if (courses == null) {
            courseList = courseMapper.listAllByMajors(CourseConstants.getTerm(), user.getMajor());
            cacheService.setEx(CourseConstants.COURSE_MAJOR_IN_THIS_TERM + user.getMajor(),
                    JSON.toJSONString(courseList),
                    RedisExpireUtil.RandomTime(),
                    TimeUnit.MINUTES);
        } else {
            courseList = JSON.parseArray(courses, Course.class);
        }

        return getCourseTreeListResult(dto, user, courseList);
    }


    @Override
    public ResponseResult treeList(CourseListTreeDto dto) {
        dto.checkParam();
        //获取学生信息
        Students user = getUserInfo(UserThreadLocalUtil.getUser());
        //获取所有课程
        String courses = cacheService.get(CourseConstants.COURSE_ALL_IN_THIS_TERM);
        List<Course> courseList = null;
        if (courses == null) {
            courseList = courseMapper.listAll(CourseConstants.getTerm());
            cacheService.setEx(CourseConstants.COURSE_ALL_IN_THIS_TERM,
                    JSON.toJSONString(courseList),
                    RedisExpireUtil.RandomTime(),
                    TimeUnit.MINUTES);
        } else {
            courseList = JSON.parseArray(courses, Course.class);
        }
        //获取学生已经修完的课程
        return getCourseTreeListResult(dto, user, courseList);
    }

    //获取List中的详细课程信息
    private ResponseResult getCourseTreeListResult(CourseListTreeDto dto, Students user, List<Course> courseList) {
        //获取学生已经修完的课程
        Set<Long> doneCourseIds = null;
        doneCourseIds = getDoneCourseIds(user);
        List<CourseListTreeVo> courseListTreeVoList = null;
        List<Long> rest = new ArrayList<>();
        //获取完整的课程信息
        List<Long> selectedLessonIds = new ArrayList<>();
        IPage<CourseListTreeVo> page=new Page<>(dto.getPage(),dto.getPageSize(),0);
        if (courseList != null && !courseList.isEmpty()) {
            List<Course> collect = courseList.stream().filter(courseTree -> {
                        if (dto.getCourseId() != null && !courseTree.getId().equals(dto.getCourseId())) {
                            return false;
                        }
                        if (dto.getName() != null && !dto.getName().equals(courseTree.getName())) {
                            return false;
                        }
                        if (dto.getCredit() != null && !dto.getCredit().equals(courseTree.getCredit() / 10.0)) {
                            return false;
                        }
                        return true;
                    }
            ).collect(Collectors.toList());
            long total = courseList.size();

            courseListTreeVoList =
                    collect.stream().skip((dto.getPage() - 1) * dto.getPageSize()).limit(dto.getPageSize()).map(
                            courseTree -> {
                                CourseListTreeVo courseListTreeVo = GetCacheCourseDetail(courseTree.getId());
                                if (courseListTreeVo == null) {
                                    courseListTreeVo = new CourseListTreeVo();
                                    rest.add(courseTree.getId());
                                }
                                courseListTreeVo.setId(courseTree.getId());
                                courseListTreeVo.setName(courseTree.getName());
                                courseListTreeVo.setCredit(courseTree.getCredit() / 10.0);
                                return courseListTreeVo;
                            }
                    )
                    .collect(Collectors.toList());
            if (courseListTreeVoList == null || courseListTreeVoList.isEmpty()) {
                return ResponseResult.okResult(courseListTreeVoList);
            }
            if (rest != null && !rest.isEmpty()) {
                Map<Long, CourseListTreeVo> map = new HashMap();
                GetCourseDetails(rest, map);
                for (CourseListTreeVo courseListTreeVo : courseListTreeVoList) {
                    if (map.containsKey(courseListTreeVo.getId())) {
                        CourseListTreeVo cacheGet = map.get(courseListTreeVo.getId());
                        courseListTreeVo.setLessonList(cacheGet.getLessonList());
                        courseListTreeVo.setMajorCourses(cacheGet.getMajorCourses());
                        courseListTreeVo.setPreCourseIds(cacheGet.getPreCourseIds());
                    }
                }
            }
            //判断是否可选
            for (CourseListTreeVo courseListTreeVo : courseListTreeVoList) {
                //判断条件:先修课程，专业，年级，是否修过
                boolean flag = true;
                if (courseListTreeVo.getPreCourseIds() != null)
                    for (Long preCourseId : courseListTreeVo.getPreCourseIds()) {
                        if (!doneCourseIds.contains(preCourseId)) {
                            flag = false;
                            break;
                        }
                    }
                if (flag && courseListTreeVo.getMajorCourses() != null) {
                    boolean flagtemp = false;
                    for (MajorCourse majorCours : courseListTreeVo.getMajorCourses()) {
                        if (majorCours.getMajorId().equals(user.getMajor())) {
                            flagtemp = true;
                            break;
                        }
                    }
                    flag = flagtemp;
                }
                if(doneCourseIds.contains(courseListTreeVo.getId())) {
                    flag = false;
                }
                if (courseListTreeVo.getLessonList() != null)
                    for (LessonAndTime lesson : courseListTreeVo.getLessonList()) {
                        selectedLessonIds.add(lesson.getId());

                        if (flag && lesson.getTargetGrade().equals(user.getGrade())) {
                            lesson.setSelectable(true);
                        } else {
                            lesson.setSelectable(false);
                        }
                    }
            }

            Map<Long, Lesson> selecteds =
                    lessonMapper.getSelectedByIds(selectedLessonIds).stream().collect(Collectors.toMap(Lesson::getId, lesson -> lesson));
            for (CourseListTreeVo courseListTreeVo : courseListTreeVoList) {
                if (courseListTreeVo.getLessonList() != null)
                    for (LessonAndTime lesson : courseListTreeVo.getLessonList()) {
                        lesson.setSelected(selecteds.get(lesson.getId()).getSelected());
                    }
            }
            page.setCurrent(dto.getPage());
            page.setSize(dto.getPageSize());
            page.setRecords(courseListTreeVoList);
            int temp = total % dto.getPageSize() == 0 ? 0 : 1;
            page.setPages(total/dto.getPageSize()+temp);
            page.setTotal(total);

            return ResponseResult.okResult(page);

        }
        page.setCurrent(dto.getPage());
        page.setSize(dto.getPageSize());
        page.setRecords(null);
        page.setPages(0);
        page.setTotal(0);
        return ResponseResult.okResult(page);
    }

    //获取该学生已经选过的课程id
    private Set<Long> getDoneCourseIds(Students user) {
        Set<Long> doneCourseIds;
        String doneCourseJson = cacheService.get(CourseConstants.DONE_COURSE + user.getId());
        if (doneCourseJson == null) {
            List<Long> doneLessonIds = selectLessonMapper.getSelected(user.getId(),
                    CourseConstants.getTerm());
            if (!doneLessonIds.isEmpty())
                doneCourseIds = lessonMapper.getCourseIdsByLessonIds(doneLessonIds);
            else {
                doneCourseIds = new HashSet<>();
            }
            cacheService.setEx(CourseConstants.DONE_COURSE + user.getId(), JSON.toJSONString(doneCourseIds), 10, TimeUnit.MINUTES);
        } else {
            doneCourseIds =
                    new HashSet<>(JSON.parseArray(doneCourseJson, Long.class));
        }
        return doneCourseIds;
    }

    //获取简略课程信息
    @Override
    public ResponseResult getCourseInfo(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResponseResult.okResult(null);
        }
        List<Long> rest = new ArrayList<>();
        List<Course> result = new ArrayList<>();
        for (Long id : ids) {
            String courseJson = cacheService.get(CourseConstants.COURSE_INFO + id);
            if (courseJson != null) {
                Course course = JSON.parseObject(courseJson, Course.class);
                result.add(course);
            } else {
                rest.add(id);
            }
        }
        if (rest != null && !rest.isEmpty()) {
            List<Course> courses = courseMapper.selectCourse(rest);
            for (Course course : courses) {
                cacheService.setEx(CourseConstants.COURSE_INFO + course.getId(),
                        JSON.toJSONString(course), RedisExpireUtil.RandomTime(), TimeUnit.MINUTES);
            }
            result.addAll(courses);
        }

        return ResponseResult.okResult(result);
    }

    //获取学生信息
    public Students getUserInfo(Long user) {
        String userjson = cacheService.get(UserConstants.USER_INFO + user);
        Students stu = null;
        if (userjson == null) {
            ResponseResult json = userClient.getInfo(user);
            String jsonString = JSON.toJSONString(json.getData());
            stu = JSON.parseObject(jsonString, Students.class);
        } else {
            stu = JSON.parseObject(userjson, Students.class);
        }
        return stu;
    }

    //获取详细课程信息
    public void GetCourseDetails(List<Long> courseIds, Map<Long, CourseListTreeVo> courseListVoMap) {
        List<CourseListTreeVo> courseListTreeVo = courseMapper.treeList(courseIds,
                CourseConstants.getTerm());
        for (CourseListTreeVo listTreeVo : courseListTreeVo) {
            cacheService.setEx(CourseConstants.COURSE_DETAIL + listTreeVo.getId(),
                    JSON.toJSONString(listTreeVo),
                    RedisExpireUtil.RandomTime(),
                    TimeUnit.MINUTES);
            courseListVoMap.put(listTreeVo.getId(), listTreeVo);
        }

    }

    //获取详细课程信息
    public CourseListTreeVo GetCourseDetail(Long courseId) {
        List<Long> courseIds = new ArrayList<>();
        courseIds.add(courseId);
        List<CourseListTreeVo> courseListTreeVo = courseMapper.treeList(courseIds,
                CourseConstants.getTerm());
        CourseListTreeVo result = courseListTreeVo.get(0);
        cacheService.setEx(CourseConstants.COURSE_DETAIL + result.getId(),
                JSON.toJSONString(result),
                RedisExpireUtil.RandomTime(),
                TimeUnit.MINUTES);
        return result;
    }

    //从缓存中获取详细课程信息
    public CourseListTreeVo GetCacheCourseDetail(Long id) {
        String courses = cacheService.get(CourseConstants.COURSE_DETAIL + id);
        CourseListTreeVo courseListTreeVo = null;
        if (courses == null) {
            return null;
        } else {
            courseListTreeVo = JSON.parseObject(courses, CourseListTreeVo.class);
        }
        return courseListTreeVo;
    }

    //检查是否有课程冲突
    @Override
    public ResponseResult check(Long lessonId) {
        //获取学生信息
        Students user = getUserInfo(UserThreadLocalUtil.getUser());

        //获取这个课次面向的专业，先修课程，年级，时间安排
        Long courseId = lessonMapper.getCourseIdByLessonId(lessonId, CourseConstants.getTerm());
        if (courseId == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        CourseListTreeVo courseListTreeVo = GetCacheCourseDetail(courseId);
        if (courseListTreeVo == null) {
            courseListTreeVo = GetCourseDetail(courseId);
        }

        //判断专业是否冲突
        boolean flag = false;
        for (MajorCourse majorCours : courseListTreeVo.getMajorCourses()) {
            if (majorCours.getMajorId().equals(user.getMajor())) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "该课程不开放于此专业");
        }

        //获取课次详细信息
        LessonAndTime lessonAndTime = null;
        for (LessonAndTime lesson : courseListTreeVo.getLessonList()) {
            if (lesson.getId() == lessonId) {
                lessonAndTime = lesson;
                break;
            }
        }
        if (lessonAndTime == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        if(!CourseConstants.getTerm().equals(lessonAndTime.getTerm())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"本学期不开放此课程");
        }
        //检查年级冲突
        if (!user.getGrade().equals(lessonAndTime.getTargetGrade())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "该课程不开放于此年级");
        }


        //获取课表
        ResponseResult selectTableResult = selectLessonService.listByStuId();
        if (selectTableResult.getCode() != AppHttpCodeEnum.SUCCESS.getCode()) {
            return selectTableResult;
        }
        String jsonString = JSON.toJSONString(selectTableResult.getData());
        List<LessonTime> selectTable = JSON.parseArray(jsonString, LessonTime.class);
        addTableInCache(selectTable,user.getId());

        //检查时间冲突
        for (LessonTime lessonTime : selectTable) {
            if(lessonTime.getLessonId().equals(lessonId)){
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"不能重复选课");
            }
            for (LessonTime seg : lessonAndTime.getLessonTimes()) {
                if (seg.getDayOfWeek() == lessonTime.getDayOfWeek() &&
                        !(seg.getBeginTime().compareTo(lessonTime.getEndTime()) > 0 ||
                                seg.getEndTime().compareTo(lessonTime.getBeginTime()) < 0)
                ) {
                    flag = false;
                    break;
                }
            }
            if (!flag) {
                break;
            }
        }
        if (!flag) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "课程时间冲突");
        }

        //是否有先修课程冲突
        flag = true;
        Set<Long> doneCourseIds = getDoneCourseIds(user);
        if(doneCourseIds.contains(courseId)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"已修过此课程");
        }
        for (Long preCourseId : courseListTreeVo.getPreCourseIds()) {
            if (!doneCourseIds.contains(preCourseId)) {
                flag = false;
                break;
            }
        }
        if (!flag) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "先修课程不符合条件");
        }

        return ResponseResult.okResult(lessonAndTime);
    }

    @Override
    public ResponseResult backLessonCheck(Long id) {
        Lesson lesson =
                Db.lambdaQuery(Lesson.class).eq(Lesson::getId, id).eq(Lesson::getTerm,
                        CourseConstants.getTerm()).one();
        if(lesson == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        SelectLesson selectlesson = Db.lambdaQuery(SelectLesson.class).eq(SelectLesson::getLessonId, lesson.getId())
                .eq(SelectLesson::getStudentId, UserThreadLocalUtil.getUser()).one();
        if(selectlesson != null){
            return ResponseResult.okResult(lesson);
        }
        List<LessonTime> selectTable = new ArrayList<>();
        addTableInCache(selectTable,UserThreadLocalUtil.getUser());
        for (LessonTime lessonTime : selectTable) {
            if(lessonTime.getLessonId().equals(id)){
                return ResponseResult.okResult(lesson);
            }
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"未选择这门课");
    }

    private void addTableInCache(List<LessonTime> selectTable,Long userId) {
        Set<String> members = cacheService.setMembers(CourseConstants.Lesson_Time_Cache + userId);
        if(members==null||members.isEmpty()){
            return ;
        }
        for (String member : members) {
            LessonTime lessonTime = JSON.parseObject(member, LessonTime.class);
            selectTable.add(lessonTime);
        }
    }
}

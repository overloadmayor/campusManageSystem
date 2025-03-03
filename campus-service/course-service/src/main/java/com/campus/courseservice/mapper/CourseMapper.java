package com.campus.courseservice.mapper;

import com.campus.model.course.pojos.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.model.course.vo.CourseListTreeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-01-19
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    List<CourseListTreeVo> treeList(@Param("ids")List<Long> ids, @Param("term")LocalDate term);

    List<Course> listAll(@Param("term")LocalDate term);

    List<Course> selectCourse(@Param("ids") List<Long> ids);

    List<Course> listAllByMajors(@Param("term") LocalDate term,@Param("major") Long major);
}

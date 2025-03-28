package com.campus.courseservice.mapper;

import com.campus.model.course.pojos.Lesson;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-01-21
 */
@Mapper
public interface LessonMapper extends BaseMapper<Lesson> {

    Set<Long> getCourseIdsByLessonIds(@Param("ids") List<Long> ids);

    List<Lesson> getSelectedByIds(@Param("ids") List<Long> ids);

    Long getCourseIdByLessonId(@Param("id") Long id, @Param("term")LocalDate term);

    List<Lesson> getLessonByTeacherId(@Param("teacherId") Long teacherId,
                                      @Param("term") LocalDate term);
}

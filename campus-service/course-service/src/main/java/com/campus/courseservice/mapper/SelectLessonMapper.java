package com.campus.courseservice.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.campus.model.selectLesson.pojos.SelectCourseAndStu;
import com.campus.model.selectLesson.pojos.SelectLesson;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.model.user.pojos.Students;
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
 * @since 2025-02-01
 */
@Mapper
public interface SelectLessonMapper extends BaseMapper<SelectLesson> {

    List<Long> getByStuId(Long user, LocalDate term);

    List<Long> getSelected(Long user,LocalDate term);

    List<SelectCourseAndStu> SelectedCourseAndStu(@Param("term") LocalDate term);

    IPage<Long> selectStuIdsByLessonId(IPage<Long> iPage, Long id);

    List<Students> selectAllStusByLessonId(Long lessonId);
}

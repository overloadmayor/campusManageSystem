package com.campus.courseservice.mapper;

import com.campus.model.course.pojos.LessonTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.model.course.pojos.LessonTimePart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-01-21
 */
@Mapper
public interface LessonTimeMapper extends BaseMapper<LessonTime> {
    List<LessonTime> getTimesByIds(@Param("ids") List<Long> ids);


//    List<LessonTimePart> calAvailableTime(Integer status);
}

package com.campus.userservice.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.model.user.pojos.Students;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-01-13
 */
@Mapper
public interface StudentsMapper extends BaseMapper<Students> {

    List<Students> getInfosByIds(@Param("ids") List<Long> ids);
}

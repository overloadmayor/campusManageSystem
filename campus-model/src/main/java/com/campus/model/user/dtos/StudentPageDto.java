package com.campus.model.user.dtos;

import com.campus.model.common.dtos.PageRequestDto;
import lombok.Data;

@Data
public class StudentPageDto extends PageRequestDto {
    private Long dept;

    private Long major;

    private Integer grade;

    private Long id;

    private String name;

    private String sex;
}

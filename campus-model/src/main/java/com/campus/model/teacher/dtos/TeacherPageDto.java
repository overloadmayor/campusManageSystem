package com.campus.model.teacher.dtos;

import com.campus.model.common.dtos.PageRequestDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TeacherPageDto extends PageRequestDto {
    private Long id;

    private String sex;

    private Long dept;

    private Integer admin;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate entryDate;

    private String title;

    private String password;

    private String name;
}

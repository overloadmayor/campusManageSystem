package com.campus.model.majorAndDept.pojos;

import com.campus.model.major.pojos.Major;
import lombok.Data;

import java.util.List;

@Data
public class DeptMajor {
    private Long Id;
    private String name;
    private List<Major> majors;
}

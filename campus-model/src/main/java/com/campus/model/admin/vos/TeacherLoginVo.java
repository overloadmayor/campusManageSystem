package com.campus.model.admin.vos;

import com.campus.model.admin.pojos.Teacher;
import com.campus.model.user.pojos.Students;
import lombok.Data;

@Data
public class TeacherLoginVo {
    private String token;
    private Teacher info;
}

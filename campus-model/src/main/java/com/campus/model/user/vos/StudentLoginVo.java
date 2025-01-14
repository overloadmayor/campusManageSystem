package com.campus.model.user.vos;

import com.campus.model.user.pojos.Students;
import lombok.Data;

@Data
public class StudentLoginVo {
    private String token;
    private Students info;
}

package com.campus.adminservice;

import com.campus.adminservice.service.ITeacherService;
import com.campus.model.admin.pojos.Teacher;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.time.LocalDate;

@SpringBootTest
class AdminServiceApplicationTests {
    @Autowired
    private ITeacherService teacherService;

    @Test
    void contextLoads() {
//        Teacher teacher = new Teacher(1L,"男",1L,0,
//                LocalDate.now(),null, DigestUtils.md5DigestAsHex("123456".getBytes()));
//        teacherService.save(teacher);
    }

}

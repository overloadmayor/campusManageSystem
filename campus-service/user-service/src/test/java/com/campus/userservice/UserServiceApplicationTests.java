package com.campus.userservice;

import com.campus.model.user.pojos.Students;
import com.campus.userservice.service.IStudentsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

@SpringBootTest
class UserServiceApplicationTests {
    @Autowired
    private IStudentsService studentsService;

    @Test
    void contextLoads() {
        Students students = new Students(2022102170L,"王梅尔","男",2022,1L,1L,24,
                DigestUtils.md5DigestAsHex("123456".getBytes()));
        studentsService.save(students);
    }

}

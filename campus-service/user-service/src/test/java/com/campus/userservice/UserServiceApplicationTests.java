package com.campus.userservice;

import cn.idev.excel.FastExcel;
import com.campus.common.constants.UserConstants;
import com.campus.model.user.pojos.StudentExcel;
import com.campus.model.user.pojos.Students;
import com.campus.userservice.listener.BatchDataListener;
import com.campus.userservice.service.IStudentsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.io.*;

@SpringBootTest
class UserServiceApplicationTests {
    @Autowired
    private IStudentsService studentsService;

    @Test
    void contextLoads() {
        File filePath = new File("D:\\heima_redis\\campusManageSystem\\tempDirectory\\849a30a4-ac1f-407e-9f51-9fd30ad8acce.xlsx");

//        System.out.println(filePath.exists());
        try (InputStream fileInputStream = new FileInputStream(filePath)) {
            FastExcel.read(fileInputStream, Students.class, new BatchDataListener(null,
                            null, null, null, null, null))
                    .sheet()
                    .doRead();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}

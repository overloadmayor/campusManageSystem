package com.campus.model.user.pojos;

import io.swagger.models.auth.In;
import lombok.Data;

import java.util.List;

@Data
public class BatchNotification extends SendMQPojos{
    //0 等待 1 完成 2 异常
    private Integer status;
    private List<Integer> rows;
}

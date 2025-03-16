package com.campus.model.user.pojos;

import lombok.Data;

@Data
public class SendMQPojos {
    private String fileTempName;
    private String fileOriginName;
    private long time;
    private Long userId;
}

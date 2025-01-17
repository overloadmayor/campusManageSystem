package com.campus.model.common.dtos;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class PageRequestDto {

    private Integer pageSize;
    private Integer page;

//    public void checkParam() {
//        if (this.page == null || this.page < 0) {
//            this.setPage(1);
//        }
//        if (this.size == null || this.size < 0 || this.size > 100) {
//            this.setSize(10);
//        }
//    }
}

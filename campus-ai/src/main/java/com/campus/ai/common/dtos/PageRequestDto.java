package com.campus.ai.common.dtos;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class PageRequestDto {

    private Integer pageSize;
    private Integer page;

    public void checkParam() {
        if (this.page == null || this.page < 0) {
            this.setPage(1);
        }
        if (this.pageSize == null || this.pageSize < 0 || this.pageSize > 100) {
            this.setPageSize(40);
        }
    }
}

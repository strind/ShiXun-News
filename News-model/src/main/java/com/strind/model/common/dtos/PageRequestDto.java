package com.strind.model.common.dtos;

import lombok.Data;

/**
 * @author strind
 * @version 1.0
 * @description 分页请求参数
 * @date 2024/3/23 10:36
 */
@Data
public class PageRequestDto {

    protected Integer size;
    protected Integer page;

    public void checkParam() {
        if (this.page == null || this.page < 0) {
            setPage(1);
        }
        if (this.size == null || this.size < 0 || this.size > 100) {
            setSize(10);
        }
    }

}

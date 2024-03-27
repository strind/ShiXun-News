package com.strind.model.common.dtos;

import com.strind.model.common.RespResult;

import java.io.Serializable;

/**
 * @author strind
 * @version 1.0
 * @description 分页结果通用结果类
 * @date 2024/3/23 10:50
 */
public class PageResponseResult extends RespResult implements Serializable {

    private Integer currentPage;
    private Integer size;
    private Integer total;

    public PageResponseResult(Integer currentPage, Integer size, Integer total) {
        this.currentPage = currentPage;
        this.size = size;
        this.total = total;
    }

    public PageResponseResult() {

    }


    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}

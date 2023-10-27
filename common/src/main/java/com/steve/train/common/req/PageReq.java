package com.steve.train.common.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

/*
 * @author     : Steve Hu
 * @date       : 2023/10/27 10:11
 * @description: 分页请求封装类
 */
public class PageReq {
    @NotNull(message = "页码不能为空")
    private Integer startPage;

    @NotNull(message = "每页条数不能为空")
    // 后端参数校验，防止前端传入过大参数
    @Max(value = 100, message = "每页条数不能超过100")
    private Integer pageSize;

    public Integer getStartPage() {
        return startPage;
    }

    public void setStartPage(Integer startPage) {
        this.startPage = startPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "PageReq{" +
                "page=" + startPage +
                ", size=" + pageSize +
                '}';
    }
}

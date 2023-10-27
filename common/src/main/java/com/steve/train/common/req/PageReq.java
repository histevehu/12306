package com.steve.train.common.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

/*
 * @author     : Steve Hu
 * @date       : 2023/10/27 10:11
 * @description: 分页请求封装类。使用时将其他业务请求封装类继承本类。
 */
public class PageReq {
    @NotNull(message = "页码不能为空")
    private Integer page;

    @NotNull(message = "每页条数不能为空")
    // 后端参数校验，防止前端传入过大参数
    @Max(value = 100, message = "每页条数不能超过100")
    private Integer size;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "PageReq{" +
                "page=" + page +
                ", size=" + size +
                '}';
    }
}

package com.steve.train.business.req;

import com.steve.train.common.req.PageReq;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/*
 * @author     : Steve Hu
 * @date       : 2023-10-31 11:29:05
 * @description: DailyTrain查询请求封装类（FreeMarker生成）
 */
public class DailyTrainQueryReq extends PageReq {
    // 对于GET请求的日期解析，使用@JsonFormat解析会报错，可以用@DateTimeFormat
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private String code;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "DailyTrainQueryReq{" +
                "date=" + date +
                ", code='" + code + '\'' +
                "} " + super.toString();
    }
}

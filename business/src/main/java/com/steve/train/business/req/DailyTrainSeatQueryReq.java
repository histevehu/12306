package com.steve.train.business.req;

import com.steve.train.common.req.PageReq;

/*
 * @author     : Steve Hu
 * @date       : 2023-11-01 11:35:34
 * @description: DailyTrainSeat查询请求封装类（FreeMarker生成）
 */
public class DailyTrainSeatQueryReq extends PageReq {

    private String trainCode;

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    @Override
    public String toString() {
        return "DailyTrainSeatQueryReq{" +
                "trainCode='" + trainCode + '\'' +
                "} " + super.toString();
    }
}

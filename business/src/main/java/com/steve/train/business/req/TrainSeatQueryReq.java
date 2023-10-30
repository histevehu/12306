package com.steve.train.business.req;

import com.steve.train.common.req.PageReq;

/*
 * @author     : Steve Hu
 * @date       : 2023-10-29 11:41:27
 * @description: TrainSeat查询请求封装类（FreeMarker生成）
 */
public class TrainSeatQueryReq extends PageReq {
    private String trainCode;

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    @Override
    public String toString() {
        return "TrainCarriageQueryReq{" +
                "trainCode='" + trainCode + '\'' +
                "} " + super.toString();
    }
}

package com.steve.train.business.dto;

import java.util.Date;

/**
 * MQ的购票消息的必要字段的封装类，和{@link com.steve.train.business.req.ConfirmOrderDoReq}相比，是简化后的消息体，仅封装了必要的字段
 */
public class ConfirmOrderMQDTO {
    /**
     * 日志流程号，用于同转异时，用同一个流水号
     */
    private String logId;

    /**
     * 日期
     */
    private Date date;

    /**
     * 车次编号
     */
    private String trainCode;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ConfirmOrderMQDto{");
        sb.append("logId=").append(logId);
        sb.append(", date=").append(date);
        sb.append(", trainCode='").append(trainCode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

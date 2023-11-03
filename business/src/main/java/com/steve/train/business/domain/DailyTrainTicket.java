package com.steve.train.business.domain;

import java.math.BigDecimal;
import java.util.Date;

public class DailyTrainTicket {
    private Long id;

    private Date date;

    private String trainCode;

    private String start;

    private String startPinyin;

    private Date startTime;

    private Integer startIndex;

    private String end;

    private String endPinyin;

    private Date endTime;

    private Integer endIndex;

    private Integer ydz;

    private BigDecimal ydzPrice;

    private Integer edz;

    private BigDecimal edzPrice;

    private Integer rw;

    private BigDecimal rwPrice;

    private Integer yw;

    private BigDecimal ywPrice;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStartPinyin() {
        return startPinyin;
    }

    public void setStartPinyin(String startPinyin) {
        this.startPinyin = startPinyin;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getEndPinyin() {
        return endPinyin;
    }

    public void setEndPinyin(String endPinyin) {
        this.endPinyin = endPinyin;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }

    public Integer getYdz() {
        return ydz;
    }

    public void setYdz(Integer ydz) {
        this.ydz = ydz;
    }

    public BigDecimal getYdzPrice() {
        return ydzPrice;
    }

    public void setYdzPrice(BigDecimal ydzPrice) {
        this.ydzPrice = ydzPrice;
    }

    public Integer getEdz() {
        return edz;
    }

    public void setEdz(Integer edz) {
        this.edz = edz;
    }

    public BigDecimal getEdzPrice() {
        return edzPrice;
    }

    public void setEdzPrice(BigDecimal edzPrice) {
        this.edzPrice = edzPrice;
    }

    public Integer getRw() {
        return rw;
    }

    public void setRw(Integer rw) {
        this.rw = rw;
    }

    public BigDecimal getRwPrice() {
        return rwPrice;
    }

    public void setRwPrice(BigDecimal rwPrice) {
        this.rwPrice = rwPrice;
    }

    public Integer getYw() {
        return yw;
    }

    public void setYw(Integer yw) {
        this.yw = yw;
    }

    public BigDecimal getYwPrice() {
        return ywPrice;
    }

    public void setYwPrice(BigDecimal ywPrice) {
        this.ywPrice = ywPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", date=").append(date);
        sb.append(", trainCode=").append(trainCode);
        sb.append(", start=").append(start);
        sb.append(", startPinyin=").append(startPinyin);
        sb.append(", startTime=").append(startTime);
        sb.append(", startIndex=").append(startIndex);
        sb.append(", end=").append(end);
        sb.append(", endPinyin=").append(endPinyin);
        sb.append(", endTime=").append(endTime);
        sb.append(", endIndex=").append(endIndex);
        sb.append(", ydz=").append(ydz);
        sb.append(", ydzPrice=").append(ydzPrice);
        sb.append(", edz=").append(edz);
        sb.append(", edzPrice=").append(edzPrice);
        sb.append(", rw=").append(rw);
        sb.append(", rwPrice=").append(rwPrice);
        sb.append(", yw=").append(yw);
        sb.append(", ywPrice=").append(ywPrice);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}
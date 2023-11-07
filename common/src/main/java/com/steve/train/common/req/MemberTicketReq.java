package com.steve.train.common.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class MemberTicketReq {

    /**
     * 乘客id
     */
    @NotNull(message = "【会员id】不能为空")
    private Long memberId;

    /**
     * 乘客id
     */
    @NotNull(message = "【乘客id】不能为空")
    private Long passengerId;

    /**
     * 乘客id
     */
    @NotNull(message = "【乘客名字】不能为空")
    private String passengerName;

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @NotNull(message = "【日期】不能为空")
    private Date trainDate;

    /**
     * 车次编号
     */
    @NotBlank(message = "【车次编号】不能为空")
    private String trainCode;

    /**
     * 箱序
     */
    @NotNull(message = "【箱序】不能为空")
    private Integer carriageIndex;

    /**
     * 排号|01, 02
     */
    @NotBlank(message = "【排号】不能为空")
    private String seatRow;

    /**
     * 列号|枚举[SeatColumnEnum]
     */
    @NotBlank(message = "【列号】不能为空")
    private String seatCol;

    /**
     * 出发站
     */
    @NotBlank(message = "【出发站】不能为空")
    private String startStation;

    /**
     * 出发时间
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    @NotNull(message = "【出发时间】不能为空")
    private Date startTime;

    /**
     * 到达站
     */
    @NotBlank(message = "【到达站】不能为空")
    private String endStation;

    /**
     * 到站时间
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    @NotNull(message = "【到站时间】不能为空")
    private Date endTime;

    /**
     * 座位类型|枚举[SeatTypeEnum]
     */
    @NotBlank(message = "【座位类型】不能为空")
    private String seatType;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public Date getTrainDate() {
        return trainDate;
    }

    public void setTrainDate(Date trainDate) {
        this.trainDate = trainDate;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public Integer getCarriageIndex() {
        return carriageIndex;
    }

    public void setCarriageIndex(Integer carriageIndex) {
        this.carriageIndex = carriageIndex;
    }

    public String getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(String seatRow) {
        this.seatRow = seatRow;
    }

    public String getSeatCol() {
        return seatCol;
    }

    public void setSeatCol(String seatCol) {
        this.seatCol = seatCol;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    @Override
    public String toString() {
        return "MemberTicketReq{" +
                "memberId=" + memberId +
                ", passengerId=" + passengerId +
                ", passengerName=" + passengerName +
                ", date=" + trainDate +
                ", trainCode='" + trainCode + '\'' +
                ", carriageIndex=" + carriageIndex +
                ", row='" + seatRow + '\'' +
                ", col='" + seatCol + '\'' +
                ", start='" + startStation + '\'' +
                ", startTime=" + startTime +
                ", end='" + endStation + '\'' +
                ", endTime=" + endTime +
                ", seatType='" + seatType + '\'' +
                '}';
    }
}


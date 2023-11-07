package com.steve.train.business.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/*
 * @author     : Steve Hu
 * @date       : 2023-11-06 14:21:19
 * @description: confirmOrder保存（新增、修改）请求封装类（FreeMarker生成）
 */
public class ConfirmOrderTicketReq {

    /**
     * 乘客ID
     */
    @NotNull(message = "【乘客ID】不能为空")
    private Long passengerId;

    /**
     * 乘客票种
     */
    @NotBlank(message = "【乘客票种】不能为空")
    private String passengerType;

    /**
     * 乘客名称
     */
    @NotBlank(message = "【乘客名称】不能为空")
    private String passengerName;

    /**
     * 乘客身份证
     */
    @NotBlank(message = "【乘客身份证】不能为空")
    private String passengerIdCard;

    /**
     * 座位类型code
     */
    @NotBlank(message = "【座位类型code】不能为空")
    private String seatTypeCode;

    /**
     * 选座，可空，值示例：A1
     */
    private String seat;

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public String getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerIdCard() {
        return passengerIdCard;
    }

    public void setPassengerIdCard(String passengerIdCard) {
        this.passengerIdCard = passengerIdCard;
    }

    public String getSeatTypeCode() {
        return seatTypeCode;
    }

    public void setSeatTypeCode(String seatTypeCode) {
        this.seatTypeCode = seatTypeCode;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ConfirmOrderTicketReq{");
        sb.append("passengerId=").append(passengerId);
        sb.append(", passengerType='").append(passengerType).append('\'');
        sb.append(", passengerName='").append(passengerName).append('\'');
        sb.append(", passengerIdCard='").append(passengerIdCard).append('\'');
        sb.append(", seatTypeCode='").append(seatTypeCode).append('\'');
        sb.append(", seat='").append(seat).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

package com.steve.train.member.req;

        import java.util.Date;
        import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/*
 * @author     : Steve Hu
 * @date       : 2023-11-07 14:53:44
 * @description: Ticket保存（新增、修改）请求封装类（FreeMarker生成）
 */
public class TicketSaveReq {

    /**
    * id
    */
    private Long id;

    /**
    * 会员id
    */
                @NotNull(message = "会员id不能为空")
    private Long memberId;

    /**
    * 乘客id
    */
                @NotNull(message = "乘客id不能为空")
    private Long passengerId;

    /**
    * 乘客姓名
    */
    private String passengerName;

    /**
    * 日期
    */
            @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
                @NotNull(message = "日期不能为空")
    private Date trainDate;

    /**
    * 车次编号
    */
                @NotBlank(message = "车次编号不能为空")
    private String trainCode;

    /**
    * 箱序
    */
                @NotNull(message = "箱序不能为空")
    private Integer carriageIndex;

    /**
    * 排号|01, 02
    */
                @NotBlank(message = "排号不能为空")
    private String seatRow;

    /**
    * 列号|枚举[SeatColEnum]
    */
                @NotBlank(message = "列号不能为空")
    private String seatCol;

    /**
    * 出发站
    */
                @NotBlank(message = "出发站不能为空")
    private String startStation;

    /**
    * 出发时间
    */
            @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
                @NotNull(message = "出发时间不能为空")
    private Date startTime;

    /**
    * 到达站
    */
                @NotBlank(message = "到达站不能为空")
    private String endStation;

    /**
    * 到站时间
    */
            @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
                @NotNull(message = "到站时间不能为空")
    private Date endTime;

    /**
    * 座位类型|枚举[SeatTypeEnum]
    */
                @NotBlank(message = "座位类型不能为空")
    private String seatType;

    /**
    * 新增时间
    */
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
    * 修改时间
    */
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    public Long getId() {
    return id;
    }

    public void setId(Long id) {
    this.id = id;
    }

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

    public Date getStartTime() {
    return startTime;
    }

    public void setStartTime(Date startTime) {
    this.startTime = startTime;
    }

    public String getEndStation() {
    return endStation;
    }

    public void setEndStation(String endStation) {
    this.endStation = endStation;
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
    sb.append(", memberId=").append(memberId);
    sb.append(", passengerId=").append(passengerId);
    sb.append(", passengerName=").append(passengerName);
    sb.append(", trainDate=").append(trainDate);
    sb.append(", trainCode=").append(trainCode);
    sb.append(", carriageIndex=").append(carriageIndex);
    sb.append(", seatRow=").append(seatRow);
    sb.append(", seatCol=").append(seatCol);
    sb.append(", startStation=").append(startStation);
    sb.append(", startTime=").append(startTime);
    sb.append(", endStation=").append(endStation);
    sb.append(", endTime=").append(endTime);
    sb.append(", seatType=").append(seatType);
    sb.append(", createTime=").append(createTime);
    sb.append(", updateTime=").append(updateTime);
sb.append("]");
return sb.toString();
}
}

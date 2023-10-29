package com.steve.train.business.req;

        import java.util.Date;
        import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/*
 * @author     : Steve Hu
 * @date       : 2023-10-29 11:41:27
 * @description: TrainSeat保存（新增、修改）请求封装类（FreeMarker生成）
 */
public class TrainSeatSaveReq {

    /**
    * id
    */
    private Long id;

    /**
    * 车次编号
    */
                @NotBlank(message = "车次编号不能为空")
    private String trainCode;

    /**
    * 厢序
    */
                @NotNull(message = "厢序不能为空")
    private Integer carriageIndex;

    /**
    * 排号|01, 02
    */
                @NotBlank(message = "排号不能为空")
    private String row;

    /**
    * 列号|枚举[SeatColEnum]
    */
                @NotBlank(message = "列号不能为空")
    private String col;

    /**
    * 座位类型|枚举[SeatTypeEnum]
    */
                @NotBlank(message = "座位类型不能为空")
    private String seatType;

    /**
    * 同车厢座序
    */
                @NotNull(message = "同车厢座序不能为空")
    private Integer carriageSeatIndex;

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

    public String getRow() {
    return row;
    }

    public void setRow(String row) {
    this.row = row;
    }

    public String getCol() {
    return col;
    }

    public void setCol(String col) {
    this.col = col;
    }

    public String getSeatType() {
    return seatType;
    }

    public void setSeatType(String seatType) {
    this.seatType = seatType;
    }

    public Integer getCarriageSeatIndex() {
    return carriageSeatIndex;
    }

    public void setCarriageSeatIndex(Integer carriageSeatIndex) {
    this.carriageSeatIndex = carriageSeatIndex;
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
    sb.append(", trainCode=").append(trainCode);
    sb.append(", carriageIndex=").append(carriageIndex);
    sb.append(", row=").append(row);
    sb.append(", col=").append(col);
    sb.append(", seatType=").append(seatType);
    sb.append(", carriageSeatIndex=").append(carriageSeatIndex);
    sb.append(", createTime=").append(createTime);
    sb.append(", updateTime=").append(updateTime);
sb.append("]");
return sb.toString();
}
}

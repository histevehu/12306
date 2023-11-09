package com.steve.train.business.req;

import com.steve.train.common.req.PageReq;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

/*
 * @author     : Steve Hu
 * @date       : 2023-11-02 15:01:09
 * @description: DailyTrainTicket查询请求封装类（FreeMarker生成）
 */
public class DailyTrainTicketQueryReq extends PageReq {


    /**
     * 日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    /**
     * 车次编号
     */
    private String trainCode;

    /**
     * 出发站
     */
    private String start;

    /**
     * 到达站
     */
    private String end;

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

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DailyTrainTicketQueryReq that)) return false;
        return Objects.equals(date, that.date) && Objects.equals(trainCode, that.trainCode) && Objects.equals(start, that.start) && Objects.equals(end, that.end) && Objects.equals(((DailyTrainTicketQueryReq) o).getPage(), that.getPage()) && Objects.equals(((DailyTrainTicketQueryReq) o).getSize(), that.getSize());
    }

    @Override
    // 若只有本类的属性，没有包含父类的page,size属性，会导致page参数发生变化时，生成的key跟之前的一样而直接读取上一页的缓存内容而发生错误
    public int hashCode() {
        return Objects.hash(date, trainCode, start, end, getPage(), getSize());
    }

    @Override
    public String toString() {
        return "DailyTrainTicketQueryReq{" +
                "date=" + date +
                ", trainCode='" + trainCode + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                "} " + super.toString();
    }
}

package com.steve.train.member.req;

import com.steve.train.common.req.PageReq;

/*
 * @author     : Steve Hu
 * @date       : 2023/10/27 8:42
 * @description: 乘客查询请求封装类
 */
public class PassengerQueryReq extends PageReq {
    private long memberId;


    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PassengerQueryReq{");
        sb.append("memberId=").append(memberId);
        sb.append('}');
        return sb.toString();
    }
}

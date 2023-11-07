package com.steve.train.member.req;

import com.steve.train.common.req.PageReq;

/*
 * @author     : Steve Hu
 * @date       : 2023-11-07 14:53:44
 * @description: Ticket查询请求封装类（FreeMarker生成）
 */
public class TicketQueryReq extends PageReq {

    private Long memberId;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "TicketQueryReq{" +
                "memberId=" + memberId +
                "} " + super.toString();
    }
}


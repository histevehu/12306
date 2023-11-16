package com.steve.train.member.controller;

import com.steve.train.common.context.MemberLoginContext;
import com.steve.train.common.resp.CommonResp;
import com.steve.train.common.resp.PageResp;
import com.steve.train.member.req.TicketQueryReq;
import com.steve.train.member.resp.TicketQueryResp;
import com.steve.train.member.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/queryList")
    public CommonResp<PageResp<TicketQueryResp>> query(@Valid TicketQueryReq req) {
        CommonResp<PageResp<TicketQueryResp>> commonResp = new CommonResp<>();
        // TicketController和TicketAdminController都调用ticketService，但TicketController在调用前向请求增加当前用户的id以仅查找当前用户的车票
        req.setMemberId(MemberLoginContext.getId());
        PageResp<TicketQueryResp> pageResp = ticketService.queryList(req);
        commonResp.setContent(pageResp);
        return commonResp;
    }

}

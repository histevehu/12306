package com.steve.train.member.controller.admin;

import com.steve.train.common.resp.CommonResp;
import com.steve.train.common.resp.PageResp;
import com.steve.train.member.req.TicketQueryReq;
import com.steve.train.member.resp.TicketQueryResp;
import com.steve.train.member.service.TicketService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * @author     : Steve Hu
 * @date       : 2023-11-07 14:53:44
 * @description: 车票接口（FreeMarker生成）
 */
@RestController
@RequestMapping("/admin/ticket")
public class TicketAdminController {

    @Resource
    private TicketService ticketService;

    @GetMapping("/queryList")
    public CommonResp<PageResp<TicketQueryResp>> queryList(@Valid TicketQueryReq req) {
        PageResp<TicketQueryResp> list = ticketService.queryList(req);
        return new CommonResp<>(list);
    }

}

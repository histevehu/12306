package com.steve.train.business.controller;

import com.steve.train.business.req.DailyTrainTicketQueryReq;
import com.steve.train.business.resp.DailyTrainTicketQueryResp;
import com.steve.train.business.service.DailyTrainTicketService;
import com.steve.train.common.resp.CommonResp;
import com.steve.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * @author     : Steve Hu
 * @date       : 2023-11-02 15:01:09
 * @description: 余票信息接口（FreeMarker生成）
 */
@RestController
@RequestMapping("/dailyTrainTicket")
public class DailyTrainTicketController {

    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    /**
     * 查询当日车票
     */
    @GetMapping("/queryList")
    public CommonResp<PageResp<DailyTrainTicketQueryResp>> queryList(@Valid DailyTrainTicketQueryReq req) {
        PageResp<DailyTrainTicketQueryResp> list = dailyTrainTicketService.queryList(req);
        return new CommonResp<>(list);
    }

    /**
     * 查询当日车票（用于演示Spring内置缓存@CachePut的强制刷新）
     */
    @GetMapping("/queryList_RW")
    public CommonResp<PageResp<DailyTrainTicketQueryResp>> queryList_RW(@Valid DailyTrainTicketQueryReq req) {
        PageResp<DailyTrainTicketQueryResp> list = dailyTrainTicketService.queryList_RW(req);
        return new CommonResp<>(list);
    }

}
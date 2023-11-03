package com.steve.train.business.controller.admin;

import com.steve.train.business.req.DailyTrainTicketQueryReq;
import com.steve.train.business.req.DailyTrainTicketSaveReq;
import com.steve.train.business.resp.DailyTrainTicketQueryResp;
import com.steve.train.business.service.DailyTrainTicketService;
import com.steve.train.common.resp.CommonResp;
import com.steve.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/*
 * @author     : Steve Hu
 * @date       : 2023-11-02 15:01:09
 * @description: 余票信息管理员接口（FreeMarker生成）
 */
@RestController
@RequestMapping("/admin/dailyTrainTicket")
public class DailyTrainTicketAdminController {

    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody DailyTrainTicketSaveReq req) {
        dailyTrainTicketService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/queryList")
    public CommonResp<PageResp<DailyTrainTicketQueryResp>> queryList(@Valid DailyTrainTicketQueryReq req) {
        PageResp<DailyTrainTicketQueryResp> list = dailyTrainTicketService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        dailyTrainTicketService.delete(id);
        return new CommonResp<>();
    }

}
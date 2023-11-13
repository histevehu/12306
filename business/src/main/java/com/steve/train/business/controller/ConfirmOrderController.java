package com.steve.train.business.controller;

import com.steve.train.business.req.ConfirmOrderDoReq;
import com.steve.train.business.service.ConfirmOrderService;
import com.steve.train.common.resp.CommonResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/*
 * @author     : Steve Hu
 * @date       : 2023-11-06 14:21:19
 * @description: 确认订单管理员接口（FreeMarker生成）
 */
@RestController
@RequestMapping("/confirmOrder")
public class ConfirmOrderController {

    @Resource
    private ConfirmOrderService confirmOrderService;

    @PostMapping("/do")
    public CommonResp<Object> doConfirm(@Valid @RequestBody ConfirmOrderDoReq req) throws InterruptedException {
        confirmOrderService.doConfirm(req);
        return new CommonResp<>();
    }

    @GetMapping("/queryLineCount/{id}")
    public CommonResp<Integer> queryLineCount(@PathVariable Long id) {
        Integer count = confirmOrderService.queryLineCount(id);
        return new CommonResp<>(count);
    }


}
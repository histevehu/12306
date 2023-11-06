package com.steve.train.business.controller.admin;

import com.steve.train.business.req.confirmOrderQueryReq;
import com.steve.train.business.req.confirmOrderSaveReq;
import com.steve.train.business.resp.confirmOrderQueryResp;
import com.steve.train.business.service.confirmOrderService;
import com.steve.train.common.resp.CommonResp;
import com.steve.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/*
 * @author     : Steve Hu
 * @date       : 2023-11-06 14:21:19
 * @description: 确认订单管理员接口（FreeMarker生成）
 */
@RestController
@RequestMapping("/admin/confirmOrder")
public class confirmOrderAdminController {

    @Resource
    private confirmOrderService confirmOrderService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody confirmOrderSaveReq req) {
        confirmOrderService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/queryList")
    public CommonResp<PageResp<confirmOrderQueryResp>> queryList(@Valid confirmOrderQueryReq req) {
        PageResp<confirmOrderQueryResp> list = confirmOrderService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        confirmOrderService.delete(id);
        return new CommonResp<>();
    }

}
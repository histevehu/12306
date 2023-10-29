package com.steve.train.business.controller.admin;

import com.steve.train.common.resp.CommonResp;
import com.steve.train.common.resp.PageResp;
import com.steve.train.business.req.TrainSeatQueryReq;
import com.steve.train.business.req.TrainSeatSaveReq;
import com.steve.train.business.resp.TrainSeatQueryResp;
import com.steve.train.business.service.TrainSeatService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/*
 * @author     : Steve Hu
 * @date       : 2023-10-29 11:41:27
 * @description: 座位管理员接口（FreeMarker生成）
 */
@RestController
@RequestMapping("/admin/trainSeat")
public class TrainSeatAdminController {

    @Resource
    private TrainSeatService trainSeatService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody TrainSeatSaveReq req) {
        trainSeatService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/queryList")
    public CommonResp<PageResp<TrainSeatQueryResp>> queryList(@Valid TrainSeatQueryReq req) {
        PageResp<TrainSeatQueryResp> list = trainSeatService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        trainSeatService.delete(id);
        return new CommonResp<>();
    }

}
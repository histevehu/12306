package com.steve.train.business.controller.admin;

import com.steve.train.common.resp.CommonResp;
import com.steve.train.common.resp.PageResp;
import com.steve.train.business.req.StationQueryReq;
import com.steve.train.business.req.StationSaveReq;
import com.steve.train.business.resp.StationQueryResp;
import com.steve.train.business.service.StationService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/*
 * @author     : Steve Hu
 * @date       : 2023-10-28 23:13:10
 * @description: 车站管理员接口（FreeMarker生成）
 */
@RestController
@RequestMapping("/admin/station")
public class StationAdminController {

    @Resource
    private StationService stationService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody StationSaveReq req) {
        stationService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/queryList")
    public CommonResp<PageResp<StationQueryResp>> queryList(@Valid StationQueryReq req) {
        PageResp<StationQueryResp> list = stationService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        stationService.delete(id);
        return new CommonResp<>();
    }

}
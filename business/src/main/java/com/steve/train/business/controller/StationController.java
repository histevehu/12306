package com.steve.train.business.controller;

import com.steve.train.business.resp.StationQueryResp;
import com.steve.train.business.service.StationService;
import com.steve.train.common.resp.CommonResp;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
 * @author     : Steve Hu
 * @date       : 2023-10-28 23:13:10
 * @description: 车站接口（FreeMarker生成）
 */
@RestController
@RequestMapping("/station")
public class StationController {

    @Resource
    private StationService stationService;

    @GetMapping("/queryAll")
    public CommonResp<List<StationQueryResp>> queryAll() {
        List<StationQueryResp> list = stationService.queryList();
        return new CommonResp<>(list);
    }

}
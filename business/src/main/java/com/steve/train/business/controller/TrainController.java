package com.steve.train.business.controller;

import com.steve.train.business.resp.TrainQueryResp;
import com.steve.train.business.service.TrainService;
import com.steve.train.common.resp.CommonResp;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
 * @author     : Steve Hu
 * @date       : 2023-10-29 09:38:38
 * @description: 车次接口（FreeMarker生成）
 */
@RestController
@RequestMapping("/train")
public class TrainController {

    @Resource
    private TrainService trainService;

    @GetMapping("/queryAll")
    public CommonResp<List<TrainQueryResp>> queryList() {
        List<TrainQueryResp> list = trainService.queryAll();
        return new CommonResp<>(list);
    }
}
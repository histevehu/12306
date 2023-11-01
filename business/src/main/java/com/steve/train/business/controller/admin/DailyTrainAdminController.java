package com.steve.train.business.controller.admin;

import com.steve.train.business.req.DailyTrainQueryReq;
import com.steve.train.business.req.DailyTrainSaveReq;
import com.steve.train.business.resp.DailyTrainQueryResp;
import com.steve.train.business.service.DailyTrainService;
import com.steve.train.common.resp.CommonResp;
import com.steve.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/*
 * @author     : Steve Hu
 * @date       : 2023-10-31 11:29:05
 * @description: 每日车次管理员接口（FreeMarker生成）
 */
@RestController
@RequestMapping("/admin/dailyTrain")
public class DailyTrainAdminController {

    @Resource
    private DailyTrainService dailyTrainService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody DailyTrainSaveReq req) {
        dailyTrainService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/queryList")
    public CommonResp<PageResp<DailyTrainQueryResp>> queryList(@Valid DailyTrainQueryReq req) {
        PageResp<DailyTrainQueryResp> list = dailyTrainService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        dailyTrainService.delete(id);
        return new CommonResp<>();
    }

    @GetMapping("/genDaily/{date}")
    // @PathVariable(“date”)，如果参数名和url里的一致，括号里面的date是可以省略的。
    public CommonResp<Object> genDaily(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        dailyTrainService.genDaily(date);
        return new CommonResp<>();
    }

}
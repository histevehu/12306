package com.steve.train.business.controller.admin;

import com.steve.train.business.req.SkTokenQueryReq;
import com.steve.train.business.req.SkTokenSaveReq;
import com.steve.train.business.resp.SkTokenQueryResp;
import com.steve.train.business.service.SkTokenService;
import com.steve.train.common.resp.CommonResp;
import com.steve.train.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/*
 * @author     : Steve Hu
 * @date       : 2023-11-15 14:11:05
 * @description: 秒杀令牌管理员接口（FreeMarker生成）
 */
@RestController
@RequestMapping("/admin/skToken")
public class SkTokenAdminController {

    @Resource
    private SkTokenService skTokenService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody SkTokenSaveReq req) {
        skTokenService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/queryList")
    public CommonResp<PageResp<SkTokenQueryResp>> queryList(@Valid SkTokenQueryReq req) {
        PageResp<SkTokenQueryResp> list = skTokenService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        skTokenService.delete(id);
        return new CommonResp<>();
    }

}
package com.steve.train.member.controller;

import com.steve.train.common.context.MemberLoginContext;
import com.steve.train.common.resp.CommonResp;
import com.steve.train.common.resp.PageResp;
import com.steve.train.member.req.PassengerQueryReq;
import com.steve.train.member.req.PassengerSaveReq;
import com.steve.train.member.resp.PassengerQueryResp;
import com.steve.train.member.service.PassengerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passenger")
public class PassengerController {
    @Resource
    PassengerService passengerService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody PassengerSaveReq req) {
        passengerService.save(req);
        return new CommonResp<>();
    }

    // 查询默认为Get请求，参数放在URL中
    @GetMapping("/queryList")
    // 用户端乘客信息查询，仅能查询用户自己添加的乘客。
    public CommonResp<PageResp<PassengerQueryResp>> queryList(@Valid PassengerQueryReq req) {
        // 自动从登录信息上下文中获取memberId并写入请求封装类中
        // 因为控制台管理端可以查询所有乘客信息（memberId需要为空），为了PassengerService通用性，只能将获取memberId操作写在controller中
        req.setMemberId(MemberLoginContext.getId());
        return new CommonResp<>(passengerService.queryList(req));
    }

    @DeleteMapping("/delete/{id}")
    // @PathVariable将URL中的参数和Controller的传入参数自动映射
    public CommonResp<Object> delete(@PathVariable long id) {
        passengerService.delete(id);
        return new CommonResp<>();
    }

    @GetMapping("/queryMine")
    public CommonResp<List<PassengerQueryResp>> queryMine() {
        List<PassengerQueryResp> list = passengerService.queryMine();
        return new CommonResp<>(list);
    }

    @GetMapping("/init")
    public CommonResp<Object> init() {
        passengerService.init();
        return new CommonResp<>();
    }
}

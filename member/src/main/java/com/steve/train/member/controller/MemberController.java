package com.steve.train.member.controller;

import com.steve.train.common.resp.CommonResp;
import com.steve.train.member.req.MemberLoginReq;
import com.steve.train.member.req.MemberRegisterReq;
import com.steve.train.member.req.MemberSendCodeReq;
import com.steve.train.member.resp.MemberLoginResp;
import com.steve.train.member.service.MemberService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Resource
    MemberService memberService;

    @GetMapping("/count")
    public CommonResp<Integer> count() {
        int count = memberService.count();
        CommonResp<Integer> commonResp = new CommonResp<>();
        commonResp.setContent(count);
        return commonResp;
    }

    @PostMapping("/sendCode")
    public CommonResp<String> sendCode(@Valid MemberSendCodeReq req) {
        memberService.sendCode(req);
        // 在实例化泛型类时可以省略泛型参数，编译器会根据上下文自动推断
        return new CommonResp<>();
    }

    @PostMapping("/register")
    public CommonResp<Long> register(@Valid MemberRegisterReq req) {
        long register = memberService.register(req);
        // CommonResp<Long> commonResp = new CommonResp<>();
        // commonResp.setContent(register);
        // 在实例化泛型类时可以省略泛型参数，编译器会根据上下文自动推断
        return new CommonResp<>(register);
    }

    @PostMapping("/login")
    public CommonResp<MemberLoginResp> login(@Valid MemberLoginReq req) {
        // 将MemberLoginResp封装返回类作为CommonResp封装返回类的主体
        return new CommonResp<>(memberService.login(req));
    }
}

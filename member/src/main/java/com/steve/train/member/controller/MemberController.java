package com.steve.train.member.controller;

import com.steve.train.common.resp.CommonResp;
import com.steve.train.member.req.MemberLoginReq;
import com.steve.train.member.req.MemberRegisterReq;
import com.steve.train.member.req.MemberSendCodeReq;
import com.steve.train.member.resp.MemberLoginResp;
import com.steve.train.member.service.MemberService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
    // 只有在Controller传入的封装类参数前加@Valid，封装类中的参数校验注入才能生效
    // @RequestBody主要用来接收前端传递给后端的json字符串中的数据的(请求体中的数据的)，所以只能发送POST请求
    public CommonResp<String> sendCode(@Valid @RequestBody MemberSendCodeReq req) {
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
    public CommonResp<MemberLoginResp> login(@Valid @RequestBody MemberLoginReq req) {
        // 将MemberLoginResp封装返回类作为CommonResp封装返回类的主体
        return new CommonResp<>(memberService.login(req));
    }
}

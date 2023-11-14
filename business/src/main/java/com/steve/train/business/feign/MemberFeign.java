package com.steve.train.business.feign;

import com.steve.train.common.req.MemberTicketReq;
import com.steve.train.common.resp.CommonResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

// @FeignClient(name = "member", url = "http://127.0.0.1:8081")
@FeignClient(name = "member")
public interface MemberFeign {

    @GetMapping("/member/feign/ticket/save")
    CommonResp<Object> save(@RequestBody MemberTicketReq req);

}

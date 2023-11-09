package com.steve.train.batch.feign;

import com.steve.train.common.resp.CommonResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

// @Feign声明此为feign客户端，将发起请求
// 若未引入注册中心，想要将两个模块应用的spring.application.name相关联调用，可以通过url参数
// @FeignClient(name = "business", url = "http://127.0.0.1:8082/business/admin")
// 若介入了注册中心，则feignclient(name)仅申明了应用的ip和端口，server.servlet.context-path前缀需要自己写
@FeignClient(name = "business")
public interface BusinessFeign {
    @GetMapping("/business/hello")
    String hello();

    // 对business发起get请求
    @GetMapping("business/dailyTrain/genDaily/{date}")
    CommonResp<Object> genDaily(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date);
}

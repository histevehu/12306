package com.steve.train.batch.feign;

import com.steve.train.common.resp.CommonResp;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Business Feign 熔断降级处理类
 */
@Component
public class BusinessFeignFallback implements BusinessFeign {
    @Override
    public String hello() {
        return "Business Fallback";
    }

    @Override
    public CommonResp<Object> genDaily(Date date) {
        return null;
    }
}

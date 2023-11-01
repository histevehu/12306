package com.steve.train.batch.feign;

import com.steve.train.common.resp.CommonResp;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class BusinessFeignFallback implements BusinessFeign {
    @Override
    public CommonResp<Object> genDaily(Date date) {
        return null;
    }
}

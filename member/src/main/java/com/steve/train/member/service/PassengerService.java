package com.steve.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.steve.train.common.util.SnowFlakeUtil;
import com.steve.train.member.domain.Passenger;
import com.steve.train.member.mapper.PassengerMapper;
import com.steve.train.member.req.PassengerSaveReq;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/*
 * @author     : Steve Hu
 * @date       : 2023/10/26 14:39
 * @description: 乘客服务
 */
@Service
public class PassengerService {

    @Resource
    private PassengerMapper passengerMapper;

    public void save(PassengerSaveReq req) {
        DateTime now = DateTime.now();
        Passenger passenger = BeanUtil.copyProperties(req, Passenger.class);
        passenger.setId(SnowFlakeUtil.getSnowFlakeNextId());
        passenger.setCreateTime(now);
        passenger.setUpdateTime(now);
        passengerMapper.insert(passenger);
    }
}

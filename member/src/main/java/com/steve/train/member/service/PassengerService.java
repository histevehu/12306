package com.steve.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.steve.train.common.context.MemberLoginContext;
import com.steve.train.common.util.SnowFlakeUtil;
import com.steve.train.member.domain.Passenger;
import com.steve.train.member.domain.PassengerExample;
import com.steve.train.member.mapper.PassengerMapper;
import com.steve.train.member.req.PassengerQueryReq;
import com.steve.train.member.req.PassengerSaveReq;
import com.steve.train.member.resp.PassengerQueryResp;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

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
        // 读取线程本地变量中当前用户的ID
        passenger.setMemberId(MemberLoginContext.getId());
        passenger.setCreateTime(now);
        passenger.setUpdateTime(now);
        passengerMapper.insert(passenger);
    }

    /**
     * 乘客信息查询
     *
     * @param req
     * @return PassengerQueryResp乘客查询请求结果封装类。注意：开发规范是Controller不使用持久层实体类，所以不能直接返回Passenger对象
     */
    public List<PassengerQueryResp> queryList(PassengerQueryReq req) {
        PassengerExample passengerExample = new PassengerExample();
        // 复用criteria对象。当存在多个判断添加条件时可以复用。
        // 注意：若每次都通过passengerExample.createCriteria().XXX添加条件，只能以最后一个为准
        PassengerExample.Criteria passengerCriteria = passengerExample.createCriteria();
        if (ObjectUtil.isNotNull(req.getMemberId())) {
            passengerCriteria.andMemberIdEqualTo(req.getMemberId());
        }
        // pageSize指定将所有数据分为几页，pageNum指定查询第几页数据（从1开始）。每页数据由PageHelper自动计算
        // 对这句往下遇到的第一个SQL做拦截，增加分页 limit
        PageHelper.startPage(1, 3);
        List<Passenger> passengerList = passengerMapper.selectByExample(passengerExample);
        return BeanUtil.copyToList(passengerList, PassengerQueryResp.class);
    }
}

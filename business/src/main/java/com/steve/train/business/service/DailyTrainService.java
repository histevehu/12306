package com.steve.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.steve.train.business.domain.DailyTrain;
import com.steve.train.business.domain.DailyTrainExample;
import com.steve.train.business.domain.Train;
import com.steve.train.business.mapper.DailyTrainMapper;
import com.steve.train.business.req.DailyTrainQueryReq;
import com.steve.train.business.req.DailyTrainSaveReq;
import com.steve.train.business.resp.DailyTrainQueryResp;
import com.steve.train.common.resp.PageResp;
import com.steve.train.common.util.SnowFlakeUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/*
 * @author     : Steve Hu
 * @date       : 2023-10-31 11:29:05
 * @description: 每日车次服务（FreeMarker生成）
 */
@Service
public class DailyTrainService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainService.class);

    @Resource
    private DailyTrainMapper dailyTrainMapper;

    @Resource
    private TrainService trainService;

    @Resource
    private DailyTrainStationService dailyTrainStationService;

    @Resource
    private DailyTrainCarriageService dailyTrainCarriageService;

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @Resource
    private SkTokenService skTokenService;


    public void save(DailyTrainSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrain dailyTrain = BeanUtil.copyProperties(req, DailyTrain.class);
        if (ObjectUtil.isNull(dailyTrain.getId())) {
            dailyTrain.setId(SnowFlakeUtil.getSnowFlakeNextId());
            dailyTrain.setCreateTime(now);
            dailyTrain.setUpdateTime(now);
            dailyTrainMapper.insert(dailyTrain);
        } else {
            dailyTrain.setUpdateTime(now);
            dailyTrainMapper.updateByPrimaryKey(dailyTrain);
        }
    }

    public PageResp<DailyTrainQueryResp> queryList(DailyTrainQueryReq req) {
        DailyTrainExample dailyTrainExample = new DailyTrainExample();
        dailyTrainExample.setOrderByClause("date desc, code asc");
        DailyTrainExample.Criteria criteria = dailyTrainExample.createCriteria();
        if (ObjectUtil.isNotNull(req.getDate())) {
            criteria.andDateEqualTo(req.getDate());
        }
        if (ObjectUtil.isNotEmpty(req.getCode())) {
            criteria.andCodeEqualTo(req.getCode());
        }

        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<DailyTrain> dailyTrainList = dailyTrainMapper.selectByExample(dailyTrainExample);

        PageInfo<DailyTrain> pageInfo = new PageInfo<>(dailyTrainList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<DailyTrainQueryResp> list = BeanUtil.copyToList(dailyTrainList, DailyTrainQueryResp.class);

        PageResp<DailyTrainQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id) {
        dailyTrainMapper.deleteByPrimaryKey(id);
    }

    /**
     * 生成某日所有车次信息，包括车次、车站、车厢、座位
     *
     * @param date
     */
    public void genDaily(Date date) {
        List<Train> trainList = trainService.selectAll();
        if (CollUtil.isEmpty(trainList)) {
            LOG.info("没有车次基础数据，任务结束");
            return;
        }

        for (Train train : trainList) {
            genDailyTrain(date, train);
        }
    }

    /**
     * 封装实现生成指定日期的火车信息，包括车次、车站、车厢、座位、余票信息
     *
     * @param date
     * @param train
     */
    // 开发规范-事务嵌套：A事务中包含B子事务，那么子事务没必要加@Transactional，只要确保A加了就能起到回滚作用。
    //                但是良好的开发规范是为每个子事务也都加上。
    @Transactional
    public void genDailyTrain(Date date, Train train) {
        LOG.info("生成日期【{}】车次【{}】的信息开始", DateUtil.formatDate(date), train.getCode());
        // 删除该车次已有的数据
        DailyTrainExample dailyTrainExample = new DailyTrainExample();
        dailyTrainExample.createCriteria().andDateEqualTo(date).andCodeEqualTo(train.getCode());
        dailyTrainMapper.deleteByExample(dailyTrainExample);

        // 生成该车次的数据
        DateTime now = DateTime.now();
        DailyTrain dailyTrain = BeanUtil.copyProperties(train, DailyTrain.class);
        dailyTrain.setId(SnowFlakeUtil.getSnowFlakeNextId());
        dailyTrain.setCreateTime(now);
        dailyTrain.setUpdateTime(now);
        dailyTrain.setDate(date);
        dailyTrainMapper.insert(dailyTrain);
        // 下面的子事务，可以不用加@Transactional，只要确保本父事务加了就行。
        // 生成该车次的车站数据
        dailyTrainStationService.genDaily(date, train.getCode());
        // 生成该车次的车厢数据
        dailyTrainCarriageService.genDaily(date, train.getCode());
        // 生成该车次的座位数据
        dailyTrainSeatService.genDaily(date, train.getCode());
        // 生成该车次的余票数据
        dailyTrainTicketService.genDaily(dailyTrain, date, train.getCode());
        // 生成令牌余量数据
        skTokenService.genDaily(date, train.getCode());

        LOG.info("生成日期【{}】车次【{}】的信息结束", DateUtil.formatDate(date), train.getCode());
    }
}

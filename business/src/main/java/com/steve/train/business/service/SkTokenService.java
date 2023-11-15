package com.steve.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.steve.train.business.domain.SkToken;
import com.steve.train.business.domain.SkTokenExample;
import com.steve.train.business.mapper.SkTokenMapper;
import com.steve.train.business.mapper.custom.SkTokenMapperCust;
import com.steve.train.business.req.SkTokenQueryReq;
import com.steve.train.business.req.SkTokenSaveReq;
import com.steve.train.business.resp.SkTokenQueryResp;
import com.steve.train.common.resp.PageResp;
import com.steve.train.common.util.SnowFlakeUtil;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
 * @author     : Steve Hu
 * @date       : 2023-11-15 14:11:05
 * @description: 秒杀令牌服务（FreeMarker生成）
 */
@Service
public class SkTokenService {

    private static final Logger LOG = LoggerFactory.getLogger(SkTokenService.class);

    @Resource
    private SkTokenMapper skTokenMapper;

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

    @Resource
    private DailyTrainStationService dailyTrainStationService;

    @Resource
    private SkTokenMapperCust skTokenMapperCust;

    @Autowired
    private RedissonClient redissonClient;

    @Value("${spring.profiles.active}")
    private String env;

    /**
     * 生成每日令牌信息
     */
    public void genDaily(Date date, String trainCode) {
        LOG.info("删除日期【{}】车次【{}】的令牌记录", DateUtil.formatDate(date), trainCode);
        SkTokenExample skTokenExample = new SkTokenExample();
        skTokenExample.createCriteria().andDateEqualTo(date).andTrainCodeEqualTo(trainCode);
        skTokenMapper.deleteByExample(skTokenExample);

        DateTime now = DateTime.now();
        SkToken skToken = new SkToken();
        skToken.setDate(date);
        skToken.setTrainCode(trainCode);
        skToken.setId(SnowFlakeUtil.getSnowFlakeNextId());
        skToken.setCreateTime(now);
        skToken.setUpdateTime(now);

        int seatCount = dailyTrainSeatService.countSeat(date, trainCode);
        LOG.info("车次【{}】座位数：{}", trainCode, seatCount);

        long stationCount = dailyTrainStationService.countByTrainCode(date, trainCode);
        LOG.info("车次【{}】到站数：{}", trainCode, stationCount);

        // 3/4需要根据实际卖票比例来定，一趟火车最多可以卖（seatCount * stationCount）张火车票
        int count = (int) (seatCount * stationCount); // * 3/4);
        LOG.info("车次【{}】初始生成令牌数：{}", trainCode, count);
        skToken.setCount(count);

        skTokenMapper.insert(skToken);
    }

    public void save(SkTokenSaveReq req) {
        DateTime now = DateTime.now();
        SkToken skToken = BeanUtil.copyProperties(req, SkToken.class);
        if (ObjectUtil.isNull(skToken.getId())) {
            skToken.setId(SnowFlakeUtil.getSnowFlakeNextId());
            skToken.setCreateTime(now);
            skToken.setUpdateTime(now);
            skTokenMapper.insert(skToken);
        } else {
            skToken.setUpdateTime(now);
            skTokenMapper.updateByPrimaryKey(skToken);
        }
    }

    public PageResp<SkTokenQueryResp> queryList(SkTokenQueryReq req) {
        SkTokenExample skTokenExample = new SkTokenExample();
        skTokenExample.setOrderByClause("id desc");
        SkTokenExample.Criteria criteria = skTokenExample.createCriteria();

        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<SkToken> skTokenList = skTokenMapper.selectByExample(skTokenExample);

        PageInfo<SkToken> pageInfo = new PageInfo<>(skTokenList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<SkTokenQueryResp> list = BeanUtil.copyToList(skTokenList, SkTokenQueryResp.class);

        PageResp<SkTokenQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id) {
        skTokenMapper.deleteByPrimaryKey(id);
    }

    // TODO: 将验证精度细分到作为类型（当前令牌验证精度为车次。可能出现某一个类型座位大量用户抢购而耗尽令牌，导致其他类型的座位无法购买）
    public boolean validSkToken(Date date, String trainCode, Long memberId) {
        LOG.info("会员【{}】获取日期【{}】车次【{}】的令牌开始", memberId, DateUtil.formatDate(date), trainCode);
        String dlKey = DateUtil.formatDate(date) + "-" + trainCode;
        try {
            // 使用看门狗方案对令牌添加分布锁
            RLock lock = redissonClient.getLock(dlKey);
            boolean watchDogLock = lock.tryLock(0, TimeUnit.SECONDS);
            if (watchDogLock) {
                LOG.info("validSkToken看门狗获得分布式锁成功");
            } else {
                LOG.warn("validSkToken看门狗获得分布式锁失败");
                return false;
            }
            // 令牌约等于库存，也起到库存校验作用。令牌没有了，就不再卖票，不需要再进入购票主流程去判断库存，判断令牌肯定比判断库存效率高
            int updateCount = skTokenMapperCust.decrease(date, trainCode, 1);
            return updateCount > 0;
        } catch (InterruptedException e) {
            LOG.error("令牌获取异常:", e);
            return false;
        } finally {
            LOG.info("此处业务不应释放令牌锁，防止机器人抢票");
        }
    }
}

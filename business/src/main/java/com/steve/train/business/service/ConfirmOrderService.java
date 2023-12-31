package com.steve.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.steve.train.business.controller.RedisController;
import com.steve.train.business.domain.*;
import com.steve.train.business.dto.ConfirmOrderMQDTO;
import com.steve.train.business.enums.ConfirmOrderStatusEnum;
import com.steve.train.business.enums.RedisKeyTypeEnum;
import com.steve.train.business.enums.SeatTypeEnum;
import com.steve.train.business.mapper.ConfirmOrderMapper;
import com.steve.train.business.req.ConfirmOrderDoReq;
import com.steve.train.business.req.ConfirmOrderQueryReq;
import com.steve.train.business.req.ConfirmOrderTicketReq;
import com.steve.train.business.resp.ConfirmOrderQueryResp;
import com.steve.train.common.enums.SeatColEnum;
import com.steve.train.common.exception.BusinessException;
import com.steve.train.common.exception.BusinessExceptionEnum;
import com.steve.train.common.resp.PageResp;
import com.steve.train.common.util.SnowFlakeUtil;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
 * @author     : Steve Hu
 * @date       : 2023-11-06 14:21:19
 * @description: 确认订单服务（FreeMarker生成）
 */
@Service
public class ConfirmOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(ConfirmOrderService.class);

    @Resource
    private ConfirmOrderMapper confirmOrderMapper;
    @Resource
    private DailyTrainTicketService dailyTrainTicketService;
    @Resource
    private DailyTrainCarriageService dailyTrainCarriageService;
    @Resource
    private DailyTrainSeatService dailyTrainSeatService;
    @Resource
    private AfterConfirmOrderService afterConfirmOrderService;

    @Resource
    private SkTokenService skTokenService;

    /**
     * 注意：@AutoWired按byType自动注入，⽽@Resource默认按byName自动注入，即直接根据bean的ID进⾏注⼊。<br><br>
     * 使用JDK的@Resource:会根据变量名去查找原始类。比如，在 {@link RedisController}中我们声明了变量{@link RedisController#redisTemplate}，JDK会根据变量名查找{@link RedisTemplate}类并注入。而在这里如果我们将{@link StringRedisTemplate}类型的变量命名为{@link ConfirmOrderService#redisTemplate}则JDK会找到{@link RedisTemplate}类，这与声明的{@link StringRedisTemplate}不符，则会报错：Bean named 'redisTemplate' is expected to be of type 'org.springframework.data.redis.core.StringRedisTemplate' but was actually of type 'org.springframework.data.redis.core.RedisTemplate'。<br><br>
     * 使用Spring的@AutoWired:会根据变量类型去常量池找该类型的变量。
     */
    // @Resource
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;


    public void save(ConfirmOrderDoReq req) {
        DateTime now = DateTime.now();
        ConfirmOrder confirmOrder = BeanUtil.copyProperties(req, ConfirmOrder.class);
        if (ObjectUtil.isNull(confirmOrder.getId())) {
            confirmOrder.setId(SnowFlakeUtil.getSnowFlakeNextId());
            confirmOrder.setCreateTime(now);
            confirmOrder.setUpdateTime(now);
            confirmOrderMapper.insert(confirmOrder);
        } else {
            confirmOrder.setUpdateTime(now);
            confirmOrderMapper.updateByPrimaryKey(confirmOrder);
        }
    }

    public PageResp<ConfirmOrderQueryResp> queryList(ConfirmOrderQueryReq req) {
        ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
        confirmOrderExample.setOrderByClause("id asc");
        ConfirmOrderExample.Criteria criteria = confirmOrderExample.createCriteria();

        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<ConfirmOrder> confirmOrderList = confirmOrderMapper.selectByExample(confirmOrderExample);

        PageInfo<ConfirmOrder> pageInfo = new PageInfo<>(confirmOrderList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<ConfirmOrderQueryResp> list = BeanUtil.copyToList(confirmOrderList, ConfirmOrderQueryResp.class);

        PageResp<ConfirmOrderQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id) {
        confirmOrderMapper.deleteByPrimaryKey(id);
    }

    /**
     * 出票业务流程。
     * 注意：当一个消费者拿到了某个车次锁，则该车次下所有的票都由他来出，一张一张出，直到所有的订单都出完
     *
     * @param dto
     */
    @SentinelResource(value = "doConfirm", blockHandler = "doConfirmBlock")
    // 单机学习也使用Spring自带异步线程以替代MQ
    // 注意：和@Transactional一样，必须是外部类调用本方法才可生效，因为@Async注解会生成一个代理类，而只有当外部类调用才会生成，内部类调用本方法无效
    // @Async
    public void doConfirm(ConfirmOrderMQDTO dto) {
        // 若使用@Async或MQ异步处理启用了新的线程，原服务中的事件流水号不会自动传递过来，需要通过req对象传递
        // 同理，若使用Spring自带的@Async，也需要手动传递事件流水号
        MDC.put("LOG_ID", dto.getLogId());
        LOG.info("异步出票开始：{}", dto);
        // 为该日期该车次生成Redis分布式锁key
        String dlConfirmKey = RedisKeyTypeEnum.DL_CONFIRM_ORDER.getCode() + "-" + DateUtil.formatDate(dto.getDate()) + "-" + dto.getTrainCode();
        // 获取基本的Redis分布锁
        /* Boolean redisDL = redisTemplate.opsForValue().setIfAbsent(dlKey, dlKey, 60, TimeUnit.SECONDS);
        if (Boolean.TRUE.equals(redisDL)) {
            LOG.info("获得分布式锁，lockKey：{}", dlKey);
        } else {
            // 只是没抢到锁，并不知道票抢完了没，所以提示稍候再试
            // LOG.info("很遗憾，没抢到锁！lockKey：{}", lockKey);
            LOG.warn("获得分布式锁失败，lockKey：{}", dlKey);
            throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_LOCK_FAIL);
        }*/

        // 问题1：线程执行时间超过锁时间，会导致锁失效，从而出现超卖
        // 解决方案1：引入看门狗（守护线程）（项目主流使用此方案）：定时查询锁剩余时间，当小于一定值时自动延时。使用守护线程的好处是会随主线程的结束而结束，所以不会出现一直重置而永不过期的问题
        // 使用看门狗守护进程方案：
        RLock dlConfirm = null;
        boolean watchDogLock = false;
        try {
            dlConfirm = redissonClient.getLock(dlConfirmKey);
            watchDogLock = dlConfirm.tryLock(0, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOG.warn("看门狗尝试获得购票分布锁发生错误，{}", e.getMessage());
            throw new RuntimeException(e);
        }
        if (watchDogLock) {
            LOG.info("看门狗获得购票分布锁成功");
        } else {
            LOG.warn("看门狗获得购票分布锁失败，有其它消费线程正在出票，不做任何处理");
            // 不必抛出异常，因为MQ消费者抛出异常没有对应处理方法，没有意义
            return;
        }

        // 问题2：Redis集群宕机，不同的请求在新老结点中都获取到了锁
        // 解决方案2：Redis红锁，一个分布式锁由多个节点共同维护，每个节点通过竞争获得锁。算法要求至少半数以上的节点成功获取锁才算锁获取成功。由于开销大，项目一般很少使用。
        // 使用Redis红锁方案：（假设有3个节点，则至少要获得⌊3/2⌋+1=2个节点的锁）
            /*RLock lock1 = redissonClient1.getLock(dlKey);
            RLock lock2 = redissonClient2.getLock(dlKey);
            RLock lock3 = redissonClient3.getLock(dlKey);
            RedissonRedLock redissonRedLock=new RedissonRedLock(lock1,lock2,lock3);
            boolean tryRedLock=redissonRedLock.tryLock(0, TimeUnit.SECONDS);
            if (tryRedLock) {
            ...后续代码同看门狗方案
            */

        // 本消费者获得了该车次的购票分布锁，代表其拥有了售卖该日期车次的权限。无论有多少订单，都由本消费者来处理
        try {
            // 循环处理订单。该车次下所有的票都由本消费者来出，一张一张出，直到所有的订单都出完
            while (true) {
                // 查询确认订单表中指定日期&指定车次&处于初始状态的订单记录，
                ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
                confirmOrderExample.setOrderByClause("id asc");
                ConfirmOrderExample.Criteria criteria = confirmOrderExample.createCriteria();
                criteria.andDateEqualTo(dto.getDate())
                        .andTrainCodeEqualTo(dto.getTrainCode())
                        .andStatusEqualTo(ConfirmOrderStatusEnum.INIT.getCode());
                // 分页处理，每次取N条。后端处理大批量数据的常用做法，使用分页处理，而不是一次性查到内存里。
                PageHelper.startPage(1, 5);
                List<ConfirmOrder> list = confirmOrderMapper.selectByExampleWithBLOBs(confirmOrderExample);
                if (CollUtil.isEmpty(list)) {
                    LOG.info("没有需要处理的订单，结束循环");
                    break;
                } else {
                    LOG.info("本次处理{}条订单", list.size());
                }
                // 逐条地卖
                list.forEach(confirmOrder -> {
                    // 即使该订单出票出现异常，也不能影响处理下一个订单
                    try {
                        sell(confirmOrder);
                    } catch (BusinessException e) {
                        if (e.getE().equals(BusinessExceptionEnum.CONFIRM_ORDER_TICKET_COUNT_ERROR)) {
                            LOG.info("本订单余票不足，继续售卖下一个订单");
                            confirmOrder.setStatus(ConfirmOrderStatusEnum.EMPTY.getCode());
                            updateStatus(confirmOrder);
                        } else {
                            throw e;
                        }
                    }
                });
            }
        } finally {
            // 当分布锁非空且为当前线程所持有时，释放锁
            // 释放购票分布锁
            if (null != dlConfirm && dlConfirm.isHeldByCurrentThread()) {
                LOG.info("购票流程结束，释放购票分布锁。key：{}", dlConfirmKey);
                dlConfirm.unlock();
            }
        }

    }

    /**
     * 降级方法，需包含限流原方法的所有参数+BlockException参数
     */
    public void doConfirmBlock(ConfirmOrderDoReq req, BlockException e) {
        LOG.info("购票请求被限流：{}", req);
        throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_FLOW_EXCEPTION);
    }

    /**
     * 售票
     */
    // TODO:省略业务数据校验，如：车次是否存在、余票是否存在、车次是否在有效期内、tickets条数>0
    // TODO:省略同乘客同车次是否已经买过
    private void sell(ConfirmOrder confirmOrder) {
        // 构造ConfirmOrderDoReq
        ConfirmOrderDoReq req = new ConfirmOrderDoReq();
        req.setMemberId(confirmOrder.getMemberId());
        req.setDate(confirmOrder.getDate());
        req.setTrainCode(confirmOrder.getTrainCode());
        req.setStart(confirmOrder.getStart());
        req.setEnd(confirmOrder.getEnd());
        req.setDailyTrainTicketId(confirmOrder.getDailyTrainTicketId());
        req.setTickets(JSON.parseArray(confirmOrder.getTickets(), ConfirmOrderTicketReq.class));
        req.setImageCode("");
        req.setImageCodeToken("");
        req.setLogId("");
        // 将确认订单的状态更新为处理中，避免重复处理
        confirmOrder.setStatus(ConfirmOrderStatusEnum.PENDING.getCode());
        updateStatus(confirmOrder);
        LOG.info("confirm_order[{}]状态更新为处理中", confirmOrder.getId());

        Date date = req.getDate();
        String trainCode = req.getTrainCode();
        String start = req.getStart();
        String end = req.getEnd();
        // 查出余票记录，需要得到真实的库存
        DailyTrainTicket dailyTrainTicket = dailyTrainTicketService.selectByUnique(date, trainCode, start, end);
        LOG.info("查出余票记录：{}", dailyTrainTicket);

        // 扣减余票数量，并判断余票是否足够
        reduceTickets(req, dailyTrainTicket);

        // 计算相对于第一个座位的偏移值
        // 比如选择的是c1,d2，则偏移值是：[0,5]
        // 比如选择的是a1,b1,c1，则偏移值是：[0,1,2]
        List<ConfirmOrderTicketReq> tickets = req.getTickets();
        List<DailyTrainSeat> finalSeatList = new ArrayList<>();
        ConfirmOrderTicketReq ticketReq0 = tickets.get(0);
        // 因为如果选座则该订单所有购票都为选座，所以通过第一张票的选座字段是否为空可以判断该订单的所有车票是否为选座票
        if (StrUtil.isNotBlank(ticketReq0.getSeat())) {
            LOG.info("本次购票有选座");
            // 查出本次选座的座位类型都有哪些列，用于计算所选座位与第一个座位的偏离值
            List<SeatColEnum> colEnumList = SeatColEnum.getColsByType(ticketReq0.getSeatTypeCode());
            LOG.info("本次选座的座位类型包含的列：{}", colEnumList);
            // 组成和前端两排选座一样的列表，用于作参照的座位列表，例：referSeatList = {A1, C1, D1, F1, A2, C2, D2, F2}
            List<String> referSeatList = new ArrayList<>();
            for (int i = 1; i <= 2; i++) {
                for (SeatColEnum seatColEnum : colEnumList) {
                    referSeatList.add(seatColEnum.getCode() + i);
                }
            }
            LOG.info("用于作参照的两排座位：{}", referSeatList);

            List<Integer> offsetList = new ArrayList<>();
            // 绝对偏移值，即：在参照座位列表中的位置
            List<Integer> aboluteOffsetList = new ArrayList<>();
            for (ConfirmOrderTicketReq ticketReq : tickets) {
                int index = referSeatList.indexOf(ticketReq.getSeat());
                aboluteOffsetList.add(index);
            }
            LOG.info("计算得到所有座位的绝对偏移值：{}", aboluteOffsetList);
            for (Integer index : aboluteOffsetList) {
                int offset = index - aboluteOffsetList.get(0);
                offsetList.add(offset);
            }
            LOG.info("计算得到所有座位的相对第一个座位的偏移值：{}", offsetList);
            // 选座
            // 一个车箱一个车箱的获取座位数据
            // 挑选符合条件的座位，如果这个车箱不满足，则进入下个车箱（多个选座应该在同一个车厢内）
            getSeat(finalSeatList, date, trainCode, ticketReq0.getSeatTypeCode(), ticketReq0.getSeat().split("")[0], // 第一个座位的列名，如从A1得到A
                    offsetList, dailyTrainTicket.getStartIndex(), dailyTrainTicket.getEndIndex());

        } else {
            LOG.info("本次购票没有选座");
            for (ConfirmOrderTicketReq ticketReq : tickets) {
                getSeat(finalSeatList, date, trainCode, ticketReq.getSeatTypeCode(), null, null, dailyTrainTicket.getStartIndex(), dailyTrainTicket.getEndIndex());
            }
        }
        LOG.info("最终选座：{}", finalSeatList);

        // 选中座位后事务处理：
        // 座位表修改售卖情况sell；
        // 余票详情表修改余票；
        // 为会员增加购票记录
        // 更新确认订单为成功
        try {
            afterConfirmOrderService.afterDoConfirm(dailyTrainTicket, finalSeatList, tickets, confirmOrder);
        } catch (Exception e) {
            LOG.error("保存购票信息失败", e);
            throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_EXCEPTION);
        }
    }

    /**
     * 更新订单状态
     */
    public void updateStatus(ConfirmOrder confirmOrder) {
        ConfirmOrder confirmOrderForUpdate = new ConfirmOrder();
        confirmOrderForUpdate.setId(confirmOrder.getId());
        confirmOrderForUpdate.setUpdateTime(new Date());
        confirmOrderForUpdate.setStatus(confirmOrder.getStatus());
        confirmOrderMapper.updateByPrimaryKeySelective(confirmOrderForUpdate);
    }

    /**
     * 扣减余票
     */
    private static void reduceTickets(ConfirmOrderDoReq req, DailyTrainTicket dailyTrainTicket) {
        for (ConfirmOrderTicketReq ticketReq : req.getTickets()) {
            String seatTypeCode = ticketReq.getSeatTypeCode();
            // 循环SeatTypeEnum的枚举类的getCode方法，直到值等于目标值seatTypeCode，返回该枚举类
            SeatTypeEnum seatTypeEnum = EnumUtil.getBy(SeatTypeEnum::getCode, seatTypeCode);
            switch (seatTypeEnum) {
                case YDZ -> {
                    int countLeft = dailyTrainTicket.getYdz() - 1;
                    if (countLeft < 0) {
                        throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setYdz(countLeft);
                }
                case EDZ -> {
                    int countLeft = dailyTrainTicket.getEdz() - 1;
                    if (countLeft < 0) {
                        throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setEdz(countLeft);
                }
                case RW -> {
                    int countLeft = dailyTrainTicket.getRw() - 1;
                    if (countLeft < 0) {
                        throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setRw(countLeft);
                }
                case YW -> {
                    int countLeft = dailyTrainTicket.getYw() - 1;
                    if (countLeft < 0) {
                        throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setYw(countLeft);
                }
            }
        }
    }

    /**
     * 挑座位，如果有选座，则一次性挑完，如果无选座，则一个一个挑
     *
     * @param date
     * @param trainCode
     * @param seatType
     * @param column
     * @param offsetList
     */
    private void getSeat(List<DailyTrainSeat> finalSeatList, Date date, String trainCode, String seatType, String column, List<Integer> offsetList, Integer startIndex, Integer endIndex) {
        List<DailyTrainSeat> getSeatList;
        List<DailyTrainCarriage> carriageList = dailyTrainCarriageService.selectBySeatType(date, trainCode, seatType);
        LOG.info("共查出{}个符合条件的车厢", carriageList.size());

        // 遍历每个车厢
        for (DailyTrainCarriage dailyTrainCarriage : carriageList) {
            LOG.info("开始从车厢{}选座", dailyTrainCarriage.getIndex());
            getSeatList = new ArrayList<>();
            List<DailyTrainSeat> seatList = dailyTrainSeatService.selectByCarriage(date, trainCode, dailyTrainCarriage.getIndex());
            LOG.info("车厢{}的座位数：{}", dailyTrainCarriage.getIndex(), seatList.size());
            // 遍历当前车厢的每个座位
            for (int i = 0; i < seatList.size(); i++) {
                DailyTrainSeat dailyTrainSeat = seatList.get(i);
                // 该座位在当前车厢的绝对位置编号
                Integer seatIndex = dailyTrainSeat.getCarriageSeatIndex();
                // 该座位的列名
                String col = dailyTrainSeat.getCol();
                // 判断当前座位不能被选中过
                boolean alreadyChooseFlag = false;
                // 通过遍历已经决定的座位列表，看看当前座位是否已经被选中
                for (DailyTrainSeat finalSeat : finalSeatList) {
                    // 根据id是否存在来判断，不能判断对象，因为选中后，sell信息会被更新，对象信息变了
                    if (finalSeat.getId().equals(dailyTrainSeat.getId())) {
                        alreadyChooseFlag = true;
                        break;
                    }
                }
                if (alreadyChooseFlag) {
                    LOG.info("座位{}被选中过，不能重复选中，继续判断下一个座位", seatIndex);
                    continue;
                }

                // 判断column，有值的话要比对列号
                if (StrUtil.isBlank(column)) {
                    LOG.info("无选座");
                } else {
                    if (!column.equals(col)) {
                        LOG.info("座位{}列值不对，继续判断下一个座位，当前列值：{}，目标列值：{}", seatIndex, col, column);
                        continue;
                    }
                }

                boolean isChoose = calSell(dailyTrainSeat, startIndex, endIndex);
                if (isChoose) {
                    LOG.info("选中座位");
                    getSeatList.add(dailyTrainSeat);
                } else {
                    continue;
                }

                // 根据offset选剩下的座位
                boolean isGetAllOffsetSeat = true;
                if (CollUtil.isNotEmpty(offsetList)) {
                    LOG.info("有偏移值：{}，校验偏移的座位是否可选", offsetList);
                    // 从索引1开始，索引0就是当前已选中的票
                    for (int j = 1; j < offsetList.size(); j++) {
                        Integer offset = offsetList.get(j);
                        // 座位在库的索引是从1开始
                        // int nextIndex = seatIndex + offset - 1;
                        int nextIndex = i + offset;
                        // 有选座时，一定要是在同一个车箱
                        if (nextIndex >= seatList.size()) {
                            LOG.info("座位{}不可选，偏移后的索引超出了这个车箱的座位数", nextIndex);
                            isGetAllOffsetSeat = false;
                            break;
                        }

                        DailyTrainSeat nextDailyTrainSeat = seatList.get(nextIndex);
                        boolean isChooseNext = calSell(nextDailyTrainSeat, startIndex, endIndex);
                        if (isChooseNext) {
                            LOG.info("座位{}被选中", nextDailyTrainSeat.getCarriageSeatIndex());
                            getSeatList.add(nextDailyTrainSeat);
                        } else {
                            LOG.info("座位{}不可选", nextDailyTrainSeat.getCarriageSeatIndex());
                            isGetAllOffsetSeat = false;
                            break;
                        }
                    }
                }
                if (!isGetAllOffsetSeat) {
                    getSeatList = new ArrayList<>();
                    continue;
                }

                // 保存选好的座位
                finalSeatList.addAll(getSeatList);
                return;
            }
        }
    }

    /**
     * 计算某座位在区间内是否可卖
     * 例：sell=10001，本次购买区间站1~4，则区间已售000
     * 全部是0，表示这个区间可买；只要有1，就表示区间内已售过票
     * 选中后，要计算购票后的sell，比如原来是10001，本次购买区间站1~4
     * 方案：构造本次购票造成的售卖信息01110，和原sell 10001按位或，最终得到11111
     */
    private boolean calSell(DailyTrainSeat dailyTrainSeat, Integer startIndex, Integer endIndex) {
        // 00001, 00000
        String sell = dailyTrainSeat.getSell();
        //  000, 000
        String sellPart = sell.substring(startIndex, endIndex);
        if (Integer.parseInt(sellPart) > 0) {
            LOG.info("座位{}在本次车站区间{}~{}已售过票，不可选中该座位", dailyTrainSeat.getCarriageSeatIndex(), startIndex, endIndex);
            return false;
        } else {
            LOG.info("座位{}在本次车站区间{}~{}未售过票，可选中该座位", dailyTrainSeat.getCarriageSeatIndex(), startIndex, endIndex);
            //  111,   111
            String curSell = sellPart.replace('0', '1');
            // 0111,  0111
            curSell = StrUtil.fillBefore(curSell, '0', endIndex);
            // 01110, 01110
            curSell = StrUtil.fillAfter(curSell, '0', sell.length());

            // 当前区间售票信息curSell 01110与库里的已售信息sell 00001按位或，即可得到该座位卖出此票后的售票详情
            // 15(01111), 14(01110 = 01110|00000)
            // .binaryToInt将二进制字符串转为int整数，再做与运算
            int newSellInt = NumberUtil.binaryToInt(curSell) | NumberUtil.binaryToInt(sell);
            //  1111,  1110
            // 再将整数转为二进制字符串
            String newSell = NumberUtil.getBinaryStr(newSellInt);
            // 01111, 01110
            newSell = StrUtil.fillBefore(newSell, '0', sell.length());
            LOG.info("座位{}被选中，原售票信息：{}，车站区间：{}~{}，即：{}，最终售票信息：{}", dailyTrainSeat.getCarriageSeatIndex(), sell, startIndex, endIndex, curSell, newSell);
            dailyTrainSeat.setSell(newSell);
            return true;

        }
    }


    /**
     * 查询订单处理状态
     *
     * @param id 订单ID
     * @return 返回状态码或者实际排队数量
     */
    public Integer queryLineCount(Long id) {
        ConfirmOrder confirmOrder = confirmOrderMapper.selectByPrimaryKey(id);
        ConfirmOrderStatusEnum statusEnum = EnumUtil.getBy(ConfirmOrderStatusEnum::getCode, confirmOrder.getStatus());
        return switch (statusEnum) {
            case PENDING -> 0; // 排队0
            case SUCCESS -> -1; // 成功
            case FAILURE -> -2; // 失败
            case EMPTY -> -3; // 无票
            case CANCEL -> -4; // 取消
            case INIT -> {
                // 需要查表得到实际排队数量
                // 排在第几位，下面的写法：where a=1 and (b=1 or c=1)不支持，但等价于Mybatis支持的 where (a=1 and b=1) or (a=1 and c=1)
                ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
                // 获取该日期车次中创建时间在本订单之前的处于初始化或排队状态的订单数量
                confirmOrderExample.or().andDateEqualTo(confirmOrder.getDate())
                        .andTrainCodeEqualTo(confirmOrder.getTrainCode())
                        .andCreateTimeLessThan(confirmOrder.getCreateTime())
                        .andStatusEqualTo(ConfirmOrderStatusEnum.INIT.getCode());
                confirmOrderExample.or().andDateEqualTo(confirmOrder.getDate())
                        .andTrainCodeEqualTo(confirmOrder.getTrainCode())
                        .andCreateTimeLessThan(confirmOrder.getCreateTime())
                        .andStatusEqualTo(ConfirmOrderStatusEnum.PENDING.getCode());
                yield Math.toIntExact(confirmOrderMapper.countByExample(confirmOrderExample));
            }
        };
    }

    /**
     * 取消排队，只有I（初始）状态才能取消排队，所以按状态更新
     *
     * @param id 订单ID
     * @return 返回影响的数据条数
     */
    public Integer cancel(Long id) {
        ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
        ConfirmOrderExample.Criteria criteria = confirmOrderExample.createCriteria();
        criteria.andIdEqualTo(id).andStatusEqualTo(ConfirmOrderStatusEnum.INIT.getCode());
        ConfirmOrder confirmOrder = new ConfirmOrder();
        confirmOrder.setStatus(ConfirmOrderStatusEnum.CANCEL.getCode());
        return confirmOrderMapper.updateByExampleSelective(confirmOrder, confirmOrderExample);
    }

}

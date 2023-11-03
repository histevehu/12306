package com.steve.train.business.mapper;

import com.steve.train.business.domain.DailyTrainTicket;
import com.steve.train.business.domain.DailyTrainTicketExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DailyTrainTicketMapper {
    long countByExample(DailyTrainTicketExample example);

    int deleteByExample(DailyTrainTicketExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DailyTrainTicket record);

    int insertSelective(DailyTrainTicket record);

    List<DailyTrainTicket> selectByExample(DailyTrainTicketExample example);

    DailyTrainTicket selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DailyTrainTicket record, @Param("example") DailyTrainTicketExample example);

    int updateByExample(@Param("record") DailyTrainTicket record, @Param("example") DailyTrainTicketExample example);

    int updateByPrimaryKeySelective(DailyTrainTicket record);

    int updateByPrimaryKey(DailyTrainTicket record);
}
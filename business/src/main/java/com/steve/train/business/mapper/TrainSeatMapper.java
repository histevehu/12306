package com.steve.train.business.mapper;

import com.steve.train.business.domain.TrainSeat;
import com.steve.train.business.domain.TrainSeatExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TrainSeatMapper {
    long countByExample(TrainSeatExample example);

    int deleteByExample(TrainSeatExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TrainSeat record);

    int insertSelective(TrainSeat record);

    List<TrainSeat> selectByExample(TrainSeatExample example);

    TrainSeat selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TrainSeat record, @Param("example") TrainSeatExample example);

    int updateByExample(@Param("record") TrainSeat record, @Param("example") TrainSeatExample example);

    int updateByPrimaryKeySelective(TrainSeat record);

    int updateByPrimaryKey(TrainSeat record);
}
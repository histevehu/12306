package com.steve.train.business.mapper;

import com.steve.train.business.domain.TrainStation;
import com.steve.train.business.domain.TrainStationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TrainStationMapper {
    long countByExample(TrainStationExample example);

    int deleteByExample(TrainStationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TrainStation record);

    int insertSelective(TrainStation record);

    List<TrainStation> selectByExample(TrainStationExample example);

    TrainStation selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TrainStation record, @Param("example") TrainStationExample example);

    int updateByExample(@Param("record") TrainStation record, @Param("example") TrainStationExample example);

    int updateByPrimaryKeySelective(TrainStation record);

    int updateByPrimaryKey(TrainStation record);
}
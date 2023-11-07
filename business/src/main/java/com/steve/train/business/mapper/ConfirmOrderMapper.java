package com.steve.train.business.mapper;

import com.steve.train.business.domain.ConfirmOrder;
import com.steve.train.business.domain.ConfirmOrderExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConfirmOrderMapper {
    long countByExample(ConfirmOrderExample example);

    int deleteByExample(ConfirmOrderExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ConfirmOrder record);

    int insertSelective(ConfirmOrder record);

    List<ConfirmOrder> selectByExampleWithBLOBs(ConfirmOrderExample example);

    List<ConfirmOrder> selectByExample(ConfirmOrderExample example);

    ConfirmOrder selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ConfirmOrder record, @Param("example") ConfirmOrderExample example);

    int updateByExampleWithBLOBs(@Param("record") ConfirmOrder record, @Param("example") ConfirmOrderExample example);

    int updateByExample(@Param("record") ConfirmOrder record, @Param("example") ConfirmOrderExample example);

    int updateByPrimaryKeySelective(ConfirmOrder record);

    int updateByPrimaryKeyWithBLOBs(ConfirmOrder record);

    int updateByPrimaryKey(ConfirmOrder record);
}
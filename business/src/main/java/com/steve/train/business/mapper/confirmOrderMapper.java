package com.steve.train.business.mapper;

import com.steve.train.business.domain.confirmOrder;
import com.steve.train.business.domain.confirmOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface confirmOrderMapper {
    long countByExample(confirmOrderExample example);

    int deleteByExample(confirmOrderExample example);

    int deleteByPrimaryKey(Long id);

    int insert(confirmOrder record);

    int insertSelective(confirmOrder record);

    List<confirmOrder> selectByExampleWithBLOBs(confirmOrderExample example);

    List<confirmOrder> selectByExample(confirmOrderExample example);

    confirmOrder selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") confirmOrder record, @Param("example") confirmOrderExample example);

    int updateByExampleWithBLOBs(@Param("record") confirmOrder record, @Param("example") confirmOrderExample example);

    int updateByExample(@Param("record") confirmOrder record, @Param("example") confirmOrderExample example);

    int updateByPrimaryKeySelective(confirmOrder record);

    int updateByPrimaryKeyWithBLOBs(confirmOrder record);

    int updateByPrimaryKey(confirmOrder record);
}
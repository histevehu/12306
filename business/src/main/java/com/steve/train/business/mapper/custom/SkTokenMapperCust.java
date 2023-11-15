package com.steve.train.business.mapper.custom;

import java.util.Date;

public interface SkTokenMapperCust {
    /**
     * 减少令牌
     * @param date          指定日期
     * @param trainCode     制定车次
     * @param decreaseCount 减少令牌的数量
     * @return              记录修改数量，如果为0则表示没有符合条件的令牌
     */
    int decrease(Date date, String trainCode, int decreaseCount);
}

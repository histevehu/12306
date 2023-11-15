package com.steve.train.business.mapper.custom;

import java.util.Date;

public interface SkTokenMapperCust {
    int decrease(Date date, String trainCode, int decreaseCount);
}

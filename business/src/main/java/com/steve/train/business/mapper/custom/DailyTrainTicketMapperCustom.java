package com.steve.train.business.mapper.custom;

import java.util.Date;

public interface DailyTrainTicketMapperCustom {

    void updateCountBySell(Date date
            , String trainCode
            , String seatTypeCode
            , Integer minStartIndex
            , Integer maxStartIndex
            , Integer minEndIndex
            , Integer maxEndIndex);
}
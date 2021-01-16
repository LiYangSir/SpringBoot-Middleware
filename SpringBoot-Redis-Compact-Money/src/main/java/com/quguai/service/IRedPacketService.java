package com.quguai.service;

import com.quguai.api.RedPacketDto;

import java.math.BigDecimal;

public interface IRedPacketService {
    String handout(RedPacketDto dto);

    BigDecimal rob(Integer usrId, String redId);
}

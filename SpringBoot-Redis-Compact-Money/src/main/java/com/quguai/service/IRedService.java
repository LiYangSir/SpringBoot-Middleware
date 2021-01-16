package com.quguai.service;

import com.quguai.api.RedPacketDto;

import java.math.BigDecimal;
import java.util.List;

public interface IRedService {
    void recordRedPacket(RedPacketDto dto, String redId, List<Integer> list) throws Exception;

    void recordRobRedPacket(Integer usrId, String redId, BigDecimal amount) throws Exception;
}

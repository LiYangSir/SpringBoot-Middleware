package com.quguai.service.impl;

import com.quguai.api.RedPacketDto;
import com.quguai.dao.RedDetailRepository;
import com.quguai.dao.RedRecordRepository;
import com.quguai.dao.RedRobRecordRepository;
import com.quguai.entity.RedDetail;
import com.quguai.entity.RedRecord;
import com.quguai.entity.RedRobRecord;
import com.quguai.service.IRedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RedService implements IRedService {

    @Autowired
    private RedRecordRepository redRecordRepository;
    @Autowired
    private RedRobRecordRepository redRobRecordRepository;

    @Override
    @Async
    @Transactional
    public void recordRedPacket(RedPacketDto dto, String redId, List<Integer> list) {
        RedRecord redRecord = new RedRecord(dto.getUsrId(), redId, dto.getTotal(),
                BigDecimal.valueOf(dto.getAmount()), new Date());
        List<RedDetail> redDetails = new ArrayList<>();
        for (Integer i : list) {
            redDetails.add(new RedDetail(BigDecimal.valueOf(i), new Date(), redRecord));
        }
        redRecord.setRedDetails(redDetails);
        redRecordRepository.save(redRecord);
    }

    @Override
    @Async
    public void recordRobRedPacket(Integer usrId, String redId, BigDecimal amount) {
        RedRobRecord redRobRecord = new RedRobRecord(usrId, redId, amount, new Date());
        redRobRecordRepository.save(redRobRecord);
    }
}

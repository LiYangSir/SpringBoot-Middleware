package com.quguai.service.impl;

import com.quguai.api.RedPacketDto;
import com.quguai.api.RedPacketUtil;
import com.quguai.service.IRedPacketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedPacketService implements IRedPacketService {

    private static final String keyPrefix = "redis:red:packet";
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedService redService;
    @Override
    public String handout(RedPacketDto dto) {
        if (dto.getTotal() > 0 && dto.getAmount() > 0) {
            List<Integer> list = RedPacketUtil.divideRedPacket(dto.getAmount(), dto.getTotal());
            String timeStamp = String.valueOf(System.nanoTime());
            String redId = keyPrefix + dto.getUsrId() + ":" + timeStamp;
            redisTemplate.opsForList().leftPushAll(redId, list);
            String redTotalKey = redId + ":total";
            redisTemplate.opsForValue().set(redTotalKey, dto.getTotal());
            //保存到数据库
            redService.recordRedPacket(dto, redId, list);
            return redId;
        }
        return null;
    }

    @Override
    public BigDecimal rob(Integer usrId, String redId) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object obj = valueOperations.get(redId + usrId + ":rob");
        if (obj != null) {
            return new BigDecimal(obj.toString());
        }
        Boolean res = click(redId);
        if (res) {
            Object value = redisTemplate.opsForList().rightPop(redId);
            if (value != null) {
                String redTotalKey = redId + ":total";
                Integer currTotal = valueOperations.get(redTotalKey) != null ? (Integer) valueOperations.get(redTotalKey) : 0;
                valueOperations.set(redTotalKey, currTotal - 1);
                BigDecimal result = new BigDecimal(value.toString());
                redService.recordRobRedPacket(usrId, redId, result);
                valueOperations.set(redId + usrId + ":rob", result, 24L, TimeUnit.HOURS);
                log.info("当前用户抢到： userId:{} key:{} 金额： {}", usrId, redId, result);
                return result;
            }
        }

        return null;
    }

    private Boolean click(String redId) {
        String redTotalKey = redId + ":total";
        Object bj = redisTemplate.opsForValue().get(redTotalKey);
        if (bj != null && Integer.parseInt(bj.toString())>0)
            return true;
        return false;
    }
}

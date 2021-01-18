package com.quguai.redisson.publisher;

import com.quguai.redisson.api.RedPacketDto;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QueuePublisher {

    @Autowired
    @Qualifier("config")
    private RedissonClient redissonClient;

    public void sendMsg(RedPacketDto dto) {
        RQueue<RedPacketDto> redissonQueue = redissonClient.getQueue("redissonQueue");
        redissonQueue.add(dto);
        log.info("信息加入成功");
    }
}

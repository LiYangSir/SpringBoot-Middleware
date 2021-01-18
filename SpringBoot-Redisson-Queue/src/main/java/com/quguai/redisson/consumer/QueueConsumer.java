package com.quguai.redisson.consumer;

import com.quguai.redisson.api.RedPacketDto;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QueueConsumer implements ApplicationRunner, Ordered {

    @Autowired
    @Qualifier("config")
    private RedissonClient redissonClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        RQueue<RedPacketDto> redissonQueue = redissonClient.getQueue("redissonQueue");
        while (true) {
            RedPacketDto poll = redissonQueue.poll();
            if (poll != null) {
                log.info("receive: {}", poll);
            }
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

package com.quguai.redisson.consumer;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
@EnableScheduling
public class DelayQueueConsumer {
    
    @Autowired
    private RedissonClient redissonClient;
    
    @Scheduled(cron = "* * * * * * ?")
    public void consumerMsg() throws InterruptedException {
        RBlockingQueue<String> redissonDelayQueue = redissonClient.getBlockingQueue("redissonDelayQueue");
        String take = redissonDelayQueue.take();
        if (StringUtils.hasLength(take)) {
            log.info("receive: {}", take);
        }
    }
}

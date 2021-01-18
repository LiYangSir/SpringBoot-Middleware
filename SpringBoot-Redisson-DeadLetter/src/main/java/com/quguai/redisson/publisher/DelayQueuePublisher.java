package com.quguai.redisson.publisher;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class DelayQueuePublisher {

    @Autowired
    private RedissonClient redissonClient;

    public void sendMsg(String msg, Long ttl) {
        RBlockingQueue<Object> redissonDelayQueue = redissonClient.getBlockingQueue("redissonDelayQueue");
        RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(redissonDelayQueue);
        delayedQueue.offer(msg, ttl, TimeUnit.MILLISECONDS);
        log.info("插入成功：{}， {}", msg, ttl);
    }
}

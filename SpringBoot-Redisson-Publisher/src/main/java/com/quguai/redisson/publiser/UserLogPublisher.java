package com.quguai.redisson.publiser;

import com.quguai.redisson.api.RedPacketDto;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class UserLogPublisher {

    public static final String topicKey = "redissonUserLoginTopicKey";

    @Autowired
    @Qualifier("config")
    private RedissonClient redissonClient;

    public void sendMsg(RedPacketDto dto) {
        if (dto != null) {
            RTopic topic = redissonClient.getTopic(topicKey);
            topic.publishAsync(dto);
        }
    }
}

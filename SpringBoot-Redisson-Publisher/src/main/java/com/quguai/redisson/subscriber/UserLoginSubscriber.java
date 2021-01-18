package com.quguai.redisson.subscriber;

import com.quguai.redisson.api.RedPacketDto;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
public class UserLoginSubscriber implements ApplicationRunner, Ordered {
    public static final String topicKey = "redissonUserLoginTopicKey";

    @Autowired
    @Qualifier("config")
    private RedissonClient redissonClient;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        RTopic topic = redissonClient.getTopic(topicKey);
        topic.addListener(RedPacketDto.class, (charSequence, dto) -> {
            if (dto != null) {
                System.out.println(dto);
            }
        });
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

package com.quguai.consumer;

import com.quguai.entity.DeadInfo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class DeadConsumer {
    @RabbitListener(queues = "mq.consumer.real.queue.name")
    public void receiveMsg(DeadInfo info) {
        System.out.println(info);
    }
}

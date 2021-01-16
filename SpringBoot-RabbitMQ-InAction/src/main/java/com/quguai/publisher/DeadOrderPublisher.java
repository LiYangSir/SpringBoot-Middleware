package com.quguai.publisher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeadOrderPublisher {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMsg(Integer orderId) {
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend("mq.producer.basic.exchange.name", "mq.producer.basic.routing.key.name", orderId);
        log.info("用户下单超时，加入到死信队列， 内容为：{}", orderId);
    }
}

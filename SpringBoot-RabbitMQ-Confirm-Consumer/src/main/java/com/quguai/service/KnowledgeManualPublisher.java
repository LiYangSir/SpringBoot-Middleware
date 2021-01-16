package com.quguai.service;

import com.quguai.entity.Person;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KnowledgeManualPublisher {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(Person msg) {
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend("middle.mq.basic.info.exchange",
                "middle.mq.basic.info.routing.key",
                msg, message -> {
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return message;
                });

    }
}

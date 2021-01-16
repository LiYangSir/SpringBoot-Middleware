package com.quguai.publisher;

import com.quguai.entity.DeadInfo;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class DeadPublisher {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment environment;

    public void sendMsg(DeadInfo info) {
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend("mq.producer.basic.exchange.name", "mq.producer.basic.routing.key.name",
                info);

    }
    //message -> {
    //                    MessageProperties messageProperties = message.getMessageProperties();
    //                    messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
    //                    messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, DeadInfo.class);
    //                    return message;
    //                }
}

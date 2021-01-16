package com.quguai.config;

import com.quguai.service.KnowledgeManualConsumer;
import com.quguai.service.KnowledgeManualPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitmqConfig {

    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Autowired
    private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;

    @Autowired
    private KnowledgeManualConsumer consumer;

    @Bean("simpleListenerContainerAuto")
    public SimpleRabbitListenerContainerFactory listenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(1);
        factory.setPrefetchCount(1);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);  // 仅此一处不同
        return factory;
    }

    @Bean("simpleListenerContainerManual")
    public SimpleMessageListenerContainer simpleRabbitListenerContainer(@Qualifier("basicQueue") Queue basicQueue) {
        SimpleMessageListenerContainer factory = new SimpleMessageListenerContainer();

        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(1);
        factory.setPrefetchCount(1);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);  // 仅此一处不同
        factory.setQueues(basicQueue);
        factory.setMessageListener(consumer);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) ->
                log.info("Send Message Success: " + correlationData));
        rabbitTemplate.setReturnsCallback(returned -> {
            log.info("Message Discuss: " + returned);
        });
        return rabbitTemplate;
    }

    @Bean
    public Queue basicQueue() {
        return new Queue("middle.mq.basic.info.queue");
    }

    @Bean
    public DirectExchange basicExchange() {
        return new DirectExchange("middle.mq.basic.info.exchange");
    }

    @Bean
    public Binding basicBinding() {
        return BindingBuilder.bind(basicQueue()).to(basicExchange()).with("middle.mq.basic.info.routing.key");
    }
}

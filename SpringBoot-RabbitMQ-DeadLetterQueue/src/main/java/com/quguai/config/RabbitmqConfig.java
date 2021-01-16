package com.quguai.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Bean
    public Queue basicDeadQueue() {
        return QueueBuilder.durable("mq.dead.queue.name")
                .deadLetterExchange("mq.dead.exchange.name")
                .deadLetterRoutingKey("mq.dead.routing.key.name")
                .ttl(10000).build();
    }

    @Bean
    public TopicExchange basicProducerExchange() {
        return new TopicExchange("mq.producer.basic.exchange.name", true, false);
    }

    @Bean
    public Binding basicProducerBinding(){
        return BindingBuilder.bind(basicDeadQueue()).to(basicProducerExchange()).with("mq.producer.basic.routing.key.name");
    }

    /**
     * 创建真真意义的面向用户的队列
     */
    @Bean
    public Queue realConsumerQueue() {
        return new Queue("mq.consumer.real.queue.name", true);
    }

    @Bean
    public TopicExchange basicDeadExchange() {
        return new TopicExchange("mq.dead.exchange.name", true, false);
    }

    @Bean
    public Binding basicDeadBinding(){
        return BindingBuilder.bind(realConsumerQueue()).to(basicDeadExchange()).with("mq.dead.routing.key.name");
    }

//    @Bean("simpleListenerContainer")
//    public SimpleRabbitListenerContainerFactory listenerContainerFactory() {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//
//        factory.setConnectionFactory(connectionFactory);
//        factory.setMessageConverter(new Jackson2JsonMessageConverter());
//        factory.setConcurrentConsumers(1);
//        factory.setMaxConcurrentConsumers(1);
//        factory.setPrefetchCount(1);
//        return factory;
//    }

    @Bean
    public MessageConverter convert() {
        return new Jackson2JsonMessageConverter();
    }
}

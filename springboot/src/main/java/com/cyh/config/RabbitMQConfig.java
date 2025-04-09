package com.cyh.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    // 订单延时队列配置（15分钟延迟）
    public static final String DELAY_EXCHANGE = "stock.delay.exchange";
    public static final String DELAY_QUEUE = "stock.delay.queue";
    public static final String DELAY_ROUTING_KEY = "stock.delay.routingkey";
    public static final String DEAD_EXCHANGE = "stock.dead.exchange";
    public static final String DEAD_QUEUE = "stock.dead.queue";
    public static final String DEAD_ROUTING_KEY = "stock.dead.routingkey";

    // 声明延时队列（带死信交换器）
    @Bean
    public Queue delayQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        args.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        args.put("x-message-ttl", 60000); // 1分钟TTL（单位：毫秒）
        return new Queue(DELAY_QUEUE, true, false, false, args);
    }

    @Bean
    public DirectExchange delayExchange() {
        return new DirectExchange(DELAY_EXCHANGE);
    }

    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(delayQueue()).to(delayExchange()).with(DELAY_ROUTING_KEY);
    }

    // 声明死信队列（实际消费队列）
    @Bean
    public Queue deadQueue() {
        return new Queue(DEAD_QUEUE);
    }

    @Bean
    public DirectExchange deadExchange() {
        return new DirectExchange(DEAD_EXCHANGE);
    }

    @Bean
    public Binding deadBinding() {
        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with(DEAD_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}

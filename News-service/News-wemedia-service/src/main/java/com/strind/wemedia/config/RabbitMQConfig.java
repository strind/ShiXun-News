package com.strind.wemedia.config;

import com.strind.rabbitmq.RabbitMQConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author strind
 * @version 1.0
 * @description
 * @date 2024/3/27 10:51
 */
@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue noListenQueue(){
        return QueueBuilder.durable(RabbitMQConstants.NO_LISTEN_QUEUE)
            .deadLetterExchange(RabbitMQConstants.DEATH_EXCHANGE)
            .build();
    }

    @Bean
    public DirectExchange simpleExchange(){
        return new DirectExchange(RabbitMQConstants.SINGLE_EXCHANGE,true,false);
    }

    @Bean
    public Binding simpleBinding(){
        return BindingBuilder.bind(noListenQueue()).to(simpleExchange()).with(RabbitMQConstants.DEATH_MESSAGE);
    }


}

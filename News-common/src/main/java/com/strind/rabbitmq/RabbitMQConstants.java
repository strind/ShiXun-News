package com.strind.rabbitmq;


/**
 * @author strind
 * @version 1.0
 * @description rabbitMQ 通用常量类
 * @date 2024/3/24 16:39
 */
public class RabbitMQConstants {

    // 交换机名字

    /**
     * 死信交换机
     */
    public static final String DEATH_EXCHANGE = "deathExchange";

    public static final String SINGLE_EXCHANGE = "single_exchange";


    // rount key
    public static final String DEATH_MESSAGE = "deathMessage";
    public static final String DOWN_OR_UP_MESSAGE = "downOrUpMessage";


    // 队列名字

    /**
     * 通知文章上下架的队列
     */
    public static final String DOWN_OR_UP = "downOrUpQueue";

    /**
     * 无人监听的队列
     */
    public static final String NO_LISTEN_QUEUE = "noListenQueue";

    /**
     * 延时队列, 由消费者监听
     */
    public static final String DELAY_QUEUE = "delayQueue";

    /**
     * 延迟时间间隔 , 12小时后发表的
     */
    public static Integer DELAY_TIME = 1000 * 60 * 60 * 12;
}

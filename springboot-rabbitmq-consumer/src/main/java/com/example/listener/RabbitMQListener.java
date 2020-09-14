package com.example.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
/**
  *  @Author Liu Haonan
  *  @Date 2020/9/1 11:17
  *  @Description SpringBoot整合RabbitMQ的ConsumerListener
 *              使用Spring
  */
@Component
public class RabbitMQListener {//不需要再实现接口
    @RabbitListener(queues = "boot_topic_queue")//指定队列名
    public void listenQueue(Message message){
        System.out.println(new String(message.getBody()));//获取消息体

    }
}

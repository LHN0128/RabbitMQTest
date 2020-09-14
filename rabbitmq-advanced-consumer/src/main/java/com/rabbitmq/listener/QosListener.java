package com.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
  *  @Author Liu Haonan
  *  @Date 2020/9/2 11:23
  *  @Description Consumer端  限流机制
 *
 *  （1）确保ack消息签收为手动确认
 *  （2）配置<listener-container>的属性
 * 		perfech =1，表示消费端每次从mq拉取一条消息来消费，直到其手动确认消费完毕后再继续拉取下一条消息
 *  （3）
  */
@Component
public class QosListener implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
//        获取消息
        System.out.println(new String(message.getBody()));
//        处理业务逻辑
//        System.out.println("处理业务逻辑");
//        签收
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
    }



}

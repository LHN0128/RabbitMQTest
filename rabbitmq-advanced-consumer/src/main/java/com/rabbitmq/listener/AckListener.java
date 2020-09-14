package com.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
/**
  *  @Author Liu Haonan
  *  @Date 2020/9/1 18:45
  *  @Description 测试rabbitMQ对ConsumerACK原理。
 *                启动空的test方法会自动加载本类
 *                设置手动签收
  */
@Component
public class AckListener implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();//获取deliveryTag
        try {
//        1、接收消息
            System.out.println(new String(message.getBody()));
//        2、处理业务逻辑
            System.out.println("处理业务逻辑");

//            模拟出错,会到catch中执行，一直不断地发送消息。
//            int i = 3/0;

            //参数2： 是否需要签收多条消息
            channel.basicAck(deliveryTag,true);
        } catch (IOException e) {
//            参数3：否则重回队列。broker会重新发送消息给消费端
//            e.printStackTrace();
            channel.basicNack(deliveryTag,true,true);
        }

    }



}

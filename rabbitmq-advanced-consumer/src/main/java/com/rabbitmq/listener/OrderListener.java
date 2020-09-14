package com.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
  *  @Author Liu Haonan
  *  @Date 2020/9/2 11:23
  *  @Description 监听死信队列,实现延时队列.10s后消费订单数据
  */
@Component
public class OrderListener implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
//        获取消息
            System.out.println(new String(message.getBody()));
//        处理业务逻辑
            System.out.println("根据订单id查询其状态");
            System.out.println("判断订单状态是否支付成功");
            System.out.println("取消订单,回滚库存");
//        签收
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
        } catch (IOException e) {
            System.out.println("出现异常,拒绝签收消息");
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),true,false);
        }
    }



}

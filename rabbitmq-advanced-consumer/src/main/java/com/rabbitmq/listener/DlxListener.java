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
 *  @Description 测试消息拒收,让消息变为死信进而加入死信队列
 */
@Component
public class DlxListener implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();//获取deliveryTag
        try {
//        1、接收消息
            System.out.println(new String(message.getBody()));
//        2、处理业务逻辑
            System.out.println("处理业务逻辑");
            //模拟出错,转到catch,出现异常时拒绝签收
            int i = 3/0;
            //参数2： 是否需要签收多条消息,手动签收
            channel.basicAck(deliveryTag,true);
        } catch (IOException e) {
//            拒绝签收消息,设置requeue=false,不重回队列
            channel.basicNack(deliveryTag,true,false);
        }

    }



}
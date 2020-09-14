package com.test.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
/**
  *  @Author Liu Haonan
  *  @Date 2020/8/28 20:15
  *  @Description workQueue模式的第一个消费者。首先启动消费者
  */
public class WorkQueues_Consumer1 {
    public static void main(String[] args) throws IOException, TimeoutException {
//        1、创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();

//        2、设置连接参数
        factory.setHost("localhost");//连接mq主机，默认值为localhost
        factory.setPort(5672);//端口，默认为5672
        factory.setVirtualHost("chou's host");//虚拟机，默认为/
        factory.setUsername("chouchou");//用户名，默认为guest
        factory.setPassword("chouchou");//密码，默认为guest
//        3、创建连接Connection
        Connection connection = factory.newConnection();

//        4、创建channel
//        这里暂时不用交换机exchange。这里的channel是consumer这边的channel和producer不一样
        Channel channel = connection.createChannel();
//        5、此处可以不创建队列，因为不是先启动consumer

//        6、接收消息
//        basicConsume(String queue, boolean autoAck, Consumer callback)
//            queue：队列名称
//            autoACK：是否自动确认
//            callback：回调对象
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("consumerTag："+consumerTag);
                System.out.println("Exchange："+envelope.getExchange()+"，routingKey："+envelope.getRoutingKey());
                System.out.println("properties："+properties);
                System.out.println("body："+new String(body));
            }
        };
        channel.basicConsume("workqueues",true,consumer);
    }
}

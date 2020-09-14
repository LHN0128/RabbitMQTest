package com.test.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
/**
  *  @Author Liu Haonan
  *  @Date 2020/8/31 13:59
  *  @Description 订阅模式--消费者2
  */
public class PubSub_Consumer2 {
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
        Channel channel = connection.createChannel();
//        5、可以创建队列，优先启动consumer
        String queueName2 = "fanout_queue2";
        channel.queueDeclare(queueName2,true,false,false,null);

//        创建消费者回调对象
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("body："+ new String(body));
                System.out.println("将日志消息保存到数据库。。。");
            }
        };
        //队列名，是否自动确定，回调对象
        channel.basicConsume(queueName2,true,consumer);
    }
}

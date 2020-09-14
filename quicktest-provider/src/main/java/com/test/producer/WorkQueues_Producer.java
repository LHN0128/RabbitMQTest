package com.test.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
  *  @Author Liu Haonan
  *  @Date 2020/8/25 19:57
  *  @Description 发送消息
  */
public class WorkQueues_Producer {
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
//        这里暂时不用交换机exchange
        Channel channel = connection.createChannel();
//        5、创建队列
      //  queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
//          1、queue：队列名称。如果没有队列，则会创建，，否则不会创建
//          2、durable：是否持久化，当mq重启后，还在
//          3、exclusive：是否独占，只能有一个消费者监听这个队列；
//                       当connection关闭时，是否删除队列
//          4、autoDelete：是否自动删除。当没有consumer时，自动删除。
//          5、arguments：参数
        channel.queueDeclare("workqueues",true,false,false,null);//设置队列名为workqueues

//        6、发送消息到队列
//        public void basicPublish(String exchange, String routingKey, AMQP.BasicProperties props, byte[] body)
//            1.exchange：交换机名称，简单模式下交换机为默认的，空字符串
//            2.routingKey：路由名称
//            3.props：配置信息
//            4、body：byte[]类型，真实发送的消息

        for (int i = 0; i < 10; i++) {
            String body = "Hello RabbitMQ---"+i;
            channel.basicPublish("","workqueues",null,body.getBytes());
        }
//        7、释放资源
        channel.close();
        connection.close();
    }
}

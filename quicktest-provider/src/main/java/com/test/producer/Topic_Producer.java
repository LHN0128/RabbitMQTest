package com.test.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
  *  @Author Liu Haonan
  *  @Date 2020/8/31 13:58
  *  @Description 通配符模式Topic--将order的日志保存到数据库，将error级别的日志保存到数据库
 *                需求：日志格式为：系统.级别
  */
public class Topic_Producer {
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
//        5、创建交换机exchange
//            使用fanout类型
        String exchangeName = "test_topic";
//        exchangeDeclare(String exchange, String type, boolean durable, boolean autoDelete, boolean internal, Map<String, Object> arguments)		exchange：交换机名称
//        type：交换机类型（四种），可以是枚举也可以是String类型的参数
//          direct：定向
//          fanout：广播
//          topic：通配符
//          headers：通过参数匹配，使用较少
//        durable：是否持久化
//        autoDelete：是否自动删除
//        internal：Erlang内部使用，一般为false
//        arguments：参数列表

        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC,true,false,null);
//        6、创建队列
        String queueName1 = "topic_queue1";
        String queueName2 = "topic_queue2";
        //队列名，是否持久化，是否独占，是否自动删除，参数
        channel.queueDeclare(queueName1,true,false,false,null);
        channel.queueDeclare(queueName2,true,false,false,null);
//        7、绑定队列和交换机
        //队列1的绑定,两种通配routingKey
        channel.queueBind(queueName1,exchangeName,"#.error");
        channel.queueBind(queueName1,exchangeName,"order.*");
        //队列2的绑定
        channel.queueBind(queueName2,exchangeName,"*.*");//任何都打印在控制台
//        8、发送消息
//        交换机名称，Routingkey，参数，发送消息内容byte[]
        String body1 = "日志信息:张三调用了findAll()方法...日志级别：info...";
        String body2 = "日志信息:李四调用了delete()方法出错...日志级别：error...";

//        发送消息
        channel.basicPublish(exchangeName,"goods.info",null,body1.getBytes());

//        9、释放资源
        channel.close();
        connection.close();
    }
}

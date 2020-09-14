package com.test.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
/**
  *  @Author Liu Haonan
  *  @Date 2020/8/31 13:59
  *  @Description 订阅模式--生产者
  */
public class PubSub_Producer {
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
        String exchangeName = "test_fanout";
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

        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT,true,false,null);
//        6、创建队列
        String queueName1 = "fanout_queue1";
        String queueName2 = "fanout_queue2";
        //队列名，是否持久化，是否独占，是否自动删除，参数
        channel.queueDeclare(queueName1,true,false,false,null);
        channel.queueDeclare(queueName2,true,false,false,null);
//        7、绑定队列和交换机
        channel.queueBind(queueName1,exchangeName,"");//如果是Fanout的交换机，routingKey就是“”
        channel.queueBind(queueName2,exchangeName,"");//如果是Fanout的交换机，routingKey就是“”
//        8、发送消息
//        交换机名称，路由key，参数，发送消息内容byte[]
        String body = "日志信息:张三调用了findAll()方法...日志级别：info...";
        channel.basicPublish(exchangeName,"",null,body.getBytes());
//        9、释放资源
        channel.close();
        connection.close();
    }
}

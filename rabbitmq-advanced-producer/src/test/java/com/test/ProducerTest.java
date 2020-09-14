package com.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-rabbitmq-producer.xml")
public class ProducerTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;
/**
  *  @Author Liu Haonan
  *  @Date 2020/9/1 15:40
  *  @Description 定义回调函数confirmCallback，用于消息可靠性投递
 *                 操作步骤：1、开启确认模式；connectionFactory中设置publisher-confirms=true
 *                         2、在rabbitTemplate中定义回调函数
 *
  */
    @Test
    public void testConfirm(){
//        定义回调函数
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            System.out.println("confirm方法被执行了");
            if (ack){
                System.out.println("exchange接收到了消息");
            }else {
                System.out.println("exchange未接受到消息，原因是"+cause);
                //做一些处理重新发送消息。。。
            }
        });
//        发送消息
        rabbitTemplate.convertAndSend("test_exchange_confirm","confirm","message confirm...");
    }
    /**
      *  @Author Liu Haonan
      *  @Date 2020/9/1 16:10
      *  @Description 回退模式
     *          1、开启回退模式：
     *          2、设置ReturnCallback
     *          3、设置Exchange处理消息的模式
     *                 3.1如果没有消息路由到queue则丢弃消息（默认）
     *                 3.2如果没有消息路由到queue，则返回给消息发送方returnCallback
      */
    @Test
    public void testReturn(){
        //设置消息未发送到队列的模式
        rabbitTemplate.setMandatory(true);
//        设置returnCallback
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            System.out.println("return callback 执行了...");
            System.out.println(message);
            System.out.println(replyCode);
            System.out.println(replyText);
            System.out.println(exchange);
            System.out.println(routingKey);
            //后续处理。。。

        });
//        发送消息，故意写错路由key
        rabbitTemplate.convertAndSend("test_exchange_confirm","confirmxxxxx","message confirm...");
    }
    /**
      *  @Author Liu Haonan
      *  @Date 2020/9/2 12:21
      *  @Description 为了测试消费端限流，，发送多个消息
      */
    @Test
    public void testSendQosSendMessage(){
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("test_exchange_confirm","confirm","测试消费限流--");
        }

    }
    /**
      *  @Author Liu Haonan
      *  @Date 2020/9/2 13:08
      *  @Description 设置队列的过期时间测试。过期时间为10000ms
      *
      */
    @Test
    public void testTTLQueue(){
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("test_ttl_exchange","ttl.message","messageTTL--"+i);
        }
    }
    /**
      *  @Author Liu Haonan
      *  @Date 2020/9/2 15:34
      *  @Description 设置消息过期时间测试。过期时间为5000ms。在发送消息时设置时间
     *                创建MessagePostProcessor设置单个消息的过期时间
      */
    @Test
    public void testTTLMessage(){//

        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {

            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration("5000");
                return message;
            }
        };
        rabbitTemplate.convertAndSend("test_ttl_exchange","ttl.message","messageTTL--",messagePostProcessor);

    }
    /**
      *  @Author Liu Haonan
      *  @Date 2020/9/3 10:54
      *  @Description 测试发送死信消息,通过消息过期时间
      */
    @Test
    public void testDeadMessageByTimeOut(){
//      测试过期时间,发送死信消息
//            发送消息
            rabbitTemplate.convertAndSend("test_exchange_dlx", "test.dlx.test", "测试消息过期");
    }
    /**
     *  @Author Liu Haonan
     *  @Date 2020/9/3 10:54
     *  @Description 测试发送死信消息,通过消息过期时间
     */
    @Test
    public void testDeadMessageByQueueLength(){
//      测试超过队列长度
        for (int i = 0; i < 20; i++) {
//            发送消息个数超过队列长度
        rabbitTemplate.convertAndSend("test_exchange_dlx", "test.dlx.test", "测试消息超过个数");
        }
    }
    /**
      *  @Author Liu Haonan
      *  @Date 2020/9/3 11:22
      *  @Description 测试消息拒收.仅仅发送一条消息
      */
    @Test
    public void testDeadMessageByReject(){

            rabbitTemplate.convertAndSend("test_exchange_dlx", "test.dlx.test", "测试消息拒收");

    }
    /**
      *  @Author Liu Haonan
      *  @Date 2020/9/3 16:24
      *  @Description 测试实现延迟队列
     *         模拟实现需求:用户下单,10s内需要完成支付.如果没有完成支付,则取消订单吧
     *         下单成功后发送消息
      */
    @Test
    public void testDelayQueue() throws InterruptedException {
        rabbitTemplate.convertAndSend("order_exchange","order.msg","订单时间:"+new Date());
        System.out.println("开始倒计时");
        for (int i = 10; i >0; --i) {
            Thread.sleep(1000);
            System.out.println(i+"...");
        }
    }

}

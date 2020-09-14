package com.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-rabbitmq-producer.xml")
public class ProducerTest {
//    1、注入RabbitTemplate
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
      *  @Author Liu Haonan
      *  @Date 2020/8/31 18:28
      *  @Description 测试简单模式，使用默认交换机，routingKey为队列名
      */
    @Test
    public void testDefault(){
        rabbitTemplate.convertAndSend("spring_queue","simple pattern test");
    }
    /**
      *  @Author Liu Haonan
      *  @Date 2020/8/31 18:28
      *  @Description 测试pub/sub模式，使用Fanout广播类型交换机，routingkey为空字符串""
      */
    @Test
    public void testFanout(){
        rabbitTemplate.convertAndSend("spring_fanout_exchange","","Fanout test");
    }
    /**
      *  @Author Liu Haonan
      *  @Date 2020/8/31 18:30
      *  @Description 测试通配符模式，使用Topic类型的交换机。routingkey可带有通配符（direct为路由模式）
     *                  这里routingkey在error后有两个单词，因此只会匹配error.#，消息只会存放在spring_topic_queue_well中
      */
    @Test
    public void testTopic(){
        rabbitTemplate.convertAndSend("spring_topic_exchange","error.log.test","topic test");
    }
}

package com.example.test;

import com.example.com.exaple.config.RabbitConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
/**
  *  @Author Liu Haonan
  *  @Date 2020/9/1 11:24
  *  @Description Springboot rabbitMQ发送消息测试
  */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProducerTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage(){
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME,"boot.haha","SpringBoot For RabbitMQ！！");
    }
}

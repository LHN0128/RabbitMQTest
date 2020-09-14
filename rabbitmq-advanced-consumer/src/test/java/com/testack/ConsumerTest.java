package com.testack;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = "classpath:spring-rabbitmq-consumer.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ConsumerTest {
    @Test
    public void test(){
        //空方法，直接加载listener
        while(true){

        }
    }


}

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class SpringQueueListener implements MessageListener {
    /**
      *  @Author Liu Haonan
      *  @Date 2020/8/31 19:43
      *  @Description 接受队列中的消息
      */
    @Override
    public void onMessage(Message message) {
        System.out.println(new String(message.getBody()));
    }
}

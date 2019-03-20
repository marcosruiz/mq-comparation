package Rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

public class ProdRabbitThread implements Runnable{

  private final String topic;
  private final int nMsg;

  public ProdRabbitThread(int nMsg, String topic){
    this.nMsg = nMsg;
    this.topic = topic;
  }

  @Override
  public void run() {
    com.rabbitmq.client.ConnectionFactory factory = new com.rabbitmq.client.ConnectionFactory();
    factory.setHost("localhost");
    try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
      channel.queueDeclare(topic, true, false, false, null);

      for(int i=0; i<nMsg; i++){
        String message = String.valueOf(i);
        channel.basicPublish("", topic, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
//        System.out.println(" [x] Sent '" + message + "'");
      }
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (TimeoutException e) {
      e.printStackTrace();
    }
  }
}

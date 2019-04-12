package mq.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsRabbitThread implements Runnable {

  private final String topic;
  private final int nMsg;

  public ConsRabbitThread(int nMsg, String topic) {
    this.nMsg = nMsg;
    this.topic = topic;
  }

  @Override
  public void run() {
    try {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost("localhost");
      factory.setPort(5672);
      factory.setUsername("guest");
      factory.setPassword("guest");
      factory.setVirtualHost("/");
      final Connection connection = factory.newConnection();
      final Channel channel = connection.createChannel();
//      channel.basicQos(1);

//      channel.queueDeclare(topic, true, false, false, null);

      String message;
      GetResponse response;
      for(int i=0; i<nMsg; i++){
        response = channel.basicGet(topic, true);
        if (response != null) {
          message = new String(response.getBody(), "UTF-8");
//          System.out.println("Received '" + message + "'");
        }
      }

      channel.close();
      connection.close();

    } catch (IOException e) {
      e.printStackTrace();
    } catch (TimeoutException e) {
      e.printStackTrace();
    }
  }
}

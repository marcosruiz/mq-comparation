package mq.kafka;

import javax.jms.JMSException;
import java.io.IOException;

public class AppKafka {

  public static void main(String[] args) throws JMSException, InterruptedException, IOException {

    int nMsg = 1000000;
    String topic = "test_1";
    String zkHost = "localhost";
    String brokerHost = "localhost";

    new MQKafka().produceAndConsume(nMsg, topic);
    //new MQKafka().produceAndConsumeMT(nMsg, topic, 1, 10);
  }
}

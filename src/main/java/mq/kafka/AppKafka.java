package mq.kafka;

import javax.jms.JMSException;

public class AppKafka {

  public static void main(String[] args) throws JMSException, InterruptedException {
    int nMsg = 10000;
    String topic = "test_1";
    new MQKafka().produceAndConsume(nMsg, topic);
//    new MQKafka().produceAndConsumeMT(nMsg, topic,1,10);
  }
}

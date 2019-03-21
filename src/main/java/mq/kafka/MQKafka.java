package mq.kafka;

import mq.MQInt;

import javax.jms.JMSException;

public class MQKafka implements MQInt {

  @Override
  public void produceAndConsume(int nMsg, String topic) throws JMSException {

  }

  @Override
  public void produce(int nMsg, String topic) {

  }

  @Override
  public void consume(int nMsg, String topic) {

  }
}

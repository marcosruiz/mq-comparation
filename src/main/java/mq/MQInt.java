package mq;

import javax.jms.JMSException;
import java.util.ArrayList;

public interface MQInt {

  void produceAndConsume(int nMsg, String topic) throws JMSException;
  void produce(int nMsg, String topic);
  void consume(int nMsg, String topic);

}

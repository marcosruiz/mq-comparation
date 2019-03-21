package mq;

import javax.jms.JMSException;
import java.util.ArrayList;

public interface MQMTInt {
  ArrayList<Thread> produceMT(int nMsg, int nProd, String topic);
  ArrayList<Thread> consumeMT(int nMsg, int nCons, String topic);
  void produceAndConsumeMT(int nMsg, String topic, int nProd, int nCons) throws JMSException, InterruptedException;
}

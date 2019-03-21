package mq.active;

import org.junit.jupiter.api.Test;

import javax.jms.JMSException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppActiveTest {

  @Test
  void checkJUnit() {
    assertEquals(2, 2);
  }

  /**
   * Secuential
   */

  @Test
  void testSec10K() throws JMSException {
    String topic = "topic";
    int nMsg = 10000;
    new MQActive().produceAndConsume(nMsg, topic);
  }

  @Test
  void testSec600K() throws JMSException {
    String topic = "topic";
    int nMsg = 600000;
    new MQActive().produceAndConsume(nMsg, topic);
  }

  @Test
  void testSec1M() throws JMSException {
    String topic = "topic";
    int nMsg = 1000000;
    new MQActive().produceAndConsume(nMsg, topic);
  }

  /**
   * Multithread
   */

  @Test
  void testMT10K() throws JMSException, InterruptedException {
    String topic = "TEST.QUEUE";
    int nMsg = 10000;
    new MQActive().produceAndConsumeMT(nMsg, topic, 1, 10);
  }

  @Test
  void tesMT600K() throws JMSException, InterruptedException {
    String topic = "topic";
    int nMsg = 600000;
    new MQActive().produceAndConsumeMT(nMsg, topic, 1, 10);
  }

  @Test
  void tesMT1M() throws JMSException, InterruptedException {
    String topic = "topic";
    int nMsg = 1000000;
    new MQActive().produceAndConsumeMT(nMsg, topic, 1, 10);
  }


}

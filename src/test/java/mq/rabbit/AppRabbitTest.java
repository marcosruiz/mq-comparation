package mq.rabbit;

import org.junit.jupiter.api.Test;

import javax.jms.JMSException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppRabbitTest {

  @Test
  void checkJUnit() {
    assertEquals(2, 2);
  }

  /**
   * Multithread
   */

  @Test
  void testMT10K() throws InterruptedException {
    String topic = "topic";
    int nMsg = 10000;
    new MQRabbit().produceAndConsumeMT(nMsg, topic, 1, 10);
  }

  @Test
  void tesMT600K() throws InterruptedException {
    String topic = "topic";
    int nMsg = 600000;
    new MQRabbit().produceAndConsumeMT(nMsg, topic, 1, 10);
  }

  @Test
  void tesMT1M() throws InterruptedException {
    String topic = "topic";
    int nMsg = 1000000;
    new MQRabbit().produceAndConsumeMT(nMsg, topic, 1, 10);
  }

  /**
   * Secuential
   */

  @Test
  void testSec10k() throws InterruptedException {
    String topic = "test_queue";
    int nMsg = 10000;
    new MQRabbit().produceAndConsume(nMsg, topic);
  }

  @Test
  void testSec600K() {
    String topic = "topic";
    int nMsg = 600000;
    new MQRabbit().produceAndConsume(nMsg, topic);
  }

  @Test
  void testSec1M() throws JMSException {
    String topic = "topic";
    int nMsg = 1000000;
    new MQRabbit().produceAndConsume(nMsg, topic);
  }
}

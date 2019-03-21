package mq.kafka;

import org.junit.jupiter.api.Test;

import javax.jms.JMSException;

import static mq.kafka.AppKafka.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Clase para testear Kafka a través de Java
 * El topic 'test_1' corresponde a una cola con 10 particiones 3 esclavos y 1 maestro
 * El topic 'test_2' corresponde a una cola con 1 particion 0 esclavos y 1 maestro
 */
public class AppKafkaTest {

  @Test
  void checkJUnit() {
    assertEquals(2,2);
  }

  /**
   * SECUENTIAL: test_2
   */

  @Test
  void testSec10KTest2() throws JMSException {
    String topic = "test_2";
    int nMsg = 10000;
    new MQKafka().produceAndConsume(nMsg,topic);
  }

  @Test
  void testSec600KTest2() throws JMSException {
    String topic = "test_2";
    int nMsg = 600000;
    new MQKafka().produceAndConsume(nMsg,topic);
  }

  @Test
  void testSec1MTest2() throws JMSException {
    String topic = "test_2";
    int nMsg = 1000000;
    new MQKafka().produceAndConsume(nMsg,topic);
  }

  @Test
  void testSec100MTest2() throws JMSException {
    String topic = "test_2";
    int nMsg = 100000000;
    new MQKafka().produceAndConsume(nMsg,topic);
  }

  /**
   * SECUENTIAL: test_1
   */

  @Test
  void testSec10KTest1() throws JMSException {
    String topic = "test_1";
    int nMsg = 10000;
    new MQKafka().produceAndConsume(nMsg,topic);
  }

  @Test
  void testSec600KTest1() throws JMSException {
    String topic = "test_1";
    int nMsg = 600000;
    new MQKafka().produceAndConsume(nMsg,topic);
  }

  @Test
  void testSec1MTest1() throws JMSException {
    String topic = "test_1";
    int nMsg = 1000000;
    new MQKafka().produceAndConsume(nMsg,topic);
  }

  /**
   * MULTITHREAD test_2
   */

  @Test
  void testMT600KTest2() throws InterruptedException, JMSException {
    String topic = "test_2";
    int nMsg = 600000;
    new MQKafka().produceAndConsumeMT(nMsg, topic, 1, 1);
  }

  @Test
  void testMT1MTest2() throws InterruptedException, JMSException {
    String topic = "test_2";
    int nMsg = 1000000;
    new MQKafka().produceAndConsumeMT(nMsg, topic, 1, 1);
  }


  /**
   * MULTITHREAD test_1
   */

  @Test
  void testMT600KTest1() throws InterruptedException, JMSException {
    String topic = "test_1";
    int nMsg = 600000;
    new MQKafka().produceAndConsumeMT(nMsg, topic, 1, 10);
  }


  @Test
  void testMT1MTest1() throws InterruptedException, JMSException {
    String topic = "test_1";
    int nMsg = 1000000;
    new MQKafka().produceAndConsumeMT(nMsg, topic, 1, 10);
  }


}

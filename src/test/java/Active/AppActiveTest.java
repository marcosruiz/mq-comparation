package Active;

import org.junit.jupiter.api.Test;

import javax.jms.JMSException;

import static Active.AppActive.mainMultiThread;
import static Active.AppActive.mainSecuential;
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
  void testSec600K() throws JMSException {
    String topic = "topic";
    int nMsg = 600000;
    mainSecuential(nMsg, topic);
  }

  @Test
  void testSec1M() throws JMSException {
    String topic = "topic";
    int nMsg = 1000000;
    mainSecuential(nMsg, topic);
  }

  /**
   * Multithread
   */

  @Test
  void tesMT600K() throws JMSException, InterruptedException {
    String topic = "topic";
    int nMsg = 600000;
    mainMultiThread(nMsg, topic, 1, 10);
  }

  @Test
  void tesMT1M() throws JMSException, InterruptedException {
    String topic = "topic";
    int nMsg = 1000000;
    mainMultiThread(nMsg, topic, 1, 10);
  }


}

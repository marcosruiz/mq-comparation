package mq.active;

import javax.jms.*;
import java.util.ArrayList;

public class AppActive {

  public static void main(String[] args) throws Exception {
    int nMsg = 10000;
    String topic = "TEST.MAIN";
//    new MQActive().produceAndConsume(nMsg, topic);
    new MQActive().produceAndConsumeMT(nMsg, topic, 1, 10);
  }
}

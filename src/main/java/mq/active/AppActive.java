package mq.active;

public class AppActive {

  public static void main(String[] args) throws Exception {
    final int nMsg = 1000000;
    final String topic = "test_queue";
//    new MQActive().produceAndConsume(nMsg, topic);
    new MQActive().produceAndConsumeMT(nMsg, topic, 1, 10);
  }
}

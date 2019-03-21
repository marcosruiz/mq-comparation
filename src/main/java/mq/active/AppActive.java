package mq.active;

public class AppActive {

  public static void main(String[] args) throws Exception {
    int nMsg = 10000;
    String topic = "test_queue";
    new MQActive().produceAndConsume(nMsg, topic);
    new MQActive().produceAndConsumeMT(nMsg, topic, 1, 10);
  }
}

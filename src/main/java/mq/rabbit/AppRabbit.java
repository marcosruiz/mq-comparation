package mq.rabbit;

public class AppRabbit {

  public static void main(String[] args) throws InterruptedException {
    final int nMsg = 10000;
    final String topic = "test_queue";
    new MQRabbit().produceAndConsume(nMsg, topic);
    new MQRabbit().produceAndConsumeMT(nMsg, topic, 1, 10);
  }
}

package mq.rabbit;

public class AppRabbit {

  public static void main(String[] args) throws InterruptedException {
    final int nMsg = 100000;
    final String topic = "my-new-queue";
    new MQRabbit().produceAndConsume(nMsg, topic);
//    new MQRabbit().produceAndConsumeMT(nMsg, topic, 1, 10);
  }
}

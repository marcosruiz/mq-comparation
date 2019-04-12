package mq.rabbit;

public class AppRabbit {

  public static void main(String[] args) throws InterruptedException {
    final int nMsg = 60000;
    final String topic = "test_2";
//    new MQRabbit().produceAndConsume(nMsg, topic);
//    new MQRabbit().consume(nMsg, topic);
//    new MQRabbit().produce(nMsg, topic);
    new MQRabbit().produceAndConsumeMT(nMsg, topic, 1, 10);
  }
}

package Rabbit;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class AppRabbit {
  final static int TOTAL_ITER = 1000;

  public static void main(String[] args) {
    produce(TOTAL_ITER);
    consume(TOTAL_ITER);
  }

  private static void consume(int messages) {
    // todo
  }

  private static void produce(int messages) {
    // todo
  }
}

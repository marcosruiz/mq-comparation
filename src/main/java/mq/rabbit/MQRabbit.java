package mq.rabbit;

import mq.MQInt;
import mq.MQMTInt;

import java.util.ArrayList;

public class MQRabbit implements MQInt, MQMTInt {


  @Override
  public void produceAndConsume(int nMsg, String topic) {

  }

  @Override
  public void produce(int nMsg, String topic) {

  }

  @Override
  public void consume(int nMsg, String topic) {

  }

  @Override
  public ArrayList<Thread> produceMT(int nMsg, int nProd, String topic) {
    return null;
  }

  @Override
  public ArrayList<Thread> consumeMT(int nMsg, int nCons, String topic) {
    return null;
  }

  @Override
  public void produceAndConsumeMT(int nMsg, String topic, int nProd, int nCons) {

  }
}

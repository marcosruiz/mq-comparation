package mq.rabbit;

import mq.MQInt;
import mq.MQMTInt;
import mq.MQStats;

import java.util.ArrayList;

public class MQRabbit implements MQInt, MQMTInt {


  @Override
  public void produceAndConsume(int nMsg, String topic) {
    //PRODUCTOR
    System.out.println("PRODUCTOR");
    long timeStart = System.currentTimeMillis();
    produce(nMsg, topic);
    MQStats.printStats(timeStart, nMsg);

    // CONSUMIDOR
    System.out.println("CONSUMIDOR");
    timeStart = System.currentTimeMillis();
    consume(nMsg, topic);
    MQStats.printStats(timeStart, nMsg);
  }

  @Override
  public void produce(int nMsg, String topic) {
    ProdRabbitThread prt = new ProdRabbitThread(nMsg,topic);
    prt.run();
  }

  @Override
  public void consume(int nMsg, String topic) {
    ConsRabbitThread crt = new ConsRabbitThread(nMsg,topic);
    crt.run();
  }

  @Override
  public ArrayList<Thread> produceMT(int nMsg, int nProd, String topic) {
    ArrayList<Thread> alProd = new ArrayList();
    int msgPerProd = nMsg / nProd;
    for (int i = 0; i < nProd; i++) {
      alProd.add(new Thread(new ProdRabbitThread(msgPerProd, topic)));
      alProd.get(i).start();
    }
    return alProd;
  }

  @Override
  public ArrayList<Thread> consumeMT(int nMsg, int nCons, String topic) {
    ArrayList<Thread> alCons = new ArrayList();
    int msgPerCons = nMsg / nCons;
    for (int i = 0; i < nCons; i++) {
      alCons.add(new Thread(new ConsRabbitThread(msgPerCons, topic)));
    }
    for (int i = 0; i < nCons; i++) {
      alCons.get(i).start();
    }
    return alCons;
  }

  @Override
  public void produceAndConsumeMT(int nMsg, String topic, int nProd, int nCons) throws InterruptedException {
    // START PRODUCERS
    System.out.println("PRODUCTOR");
    long timeStart = System.currentTimeMillis();
    ArrayList<Thread> alProd = produceMT(nMsg, nProd, topic);
    // Wait to end
    for (int i = 0; i < nProd; i++) {
      alProd.get(i).join();
    }
    MQStats.printStats(timeStart, nMsg);

    // START CONSUMERS
    System.out.println("CONSUMIDOR");
    timeStart = System.currentTimeMillis();
    ArrayList<Thread> alCons = consumeMT(nMsg, nCons, topic);
    //Wait to end
    for (int i = 0; i < nCons; i++) {
      alCons.get(i).join();
    }
    MQStats.printStats(timeStart, nMsg);
  }
}

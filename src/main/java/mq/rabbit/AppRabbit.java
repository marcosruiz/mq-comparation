package mq.rabbit;


import mq.MQStats;

import java.util.ArrayList;

public class AppRabbit {

  public static void main(String[] args) throws InterruptedException {
    final int nMsg = 10000;
    final String topic = "test_queue";
    mainSecuential(nMsg, topic);
    //    mainMultiThread(nMsg, topic, 1, 10);
  }

  public static void mainSecuential(int nMsg, String topic) {
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

  public static void mainMultiThread(int nMsg, String topic, int nProd, int nCons) throws InterruptedException {
    // START PRODUCERS
    System.out.println("PRODUCTOR");
    long timeStart = System.currentTimeMillis();
    ArrayList<Thread> alProd = produceT(nMsg, topic, nProd);
    // Wait to end
    for (int i = 0; i < nProd; i++) {
      alProd.get(i).join();
    }
    MQStats.printStats(timeStart, nMsg);

    // START CONSUMERS
    System.out.println("CONSUMIDOR");
    timeStart = System.currentTimeMillis();
    ArrayList<Thread> alCons = consumeT(nMsg, topic, nCons);
    //Wait to end
    for (int i = 0; i < nCons; i++) {
      alCons.get(i).join();
    }
    MQStats.printStats(timeStart, nMsg);
  }

  public static void produce(int nMsg, String topic) {
    ProdRabbitThread prt = new ProdRabbitThread(nMsg,topic);
    prt.run();
  }

  public static void consume(int nMsg, String topic) {
    ConsRabbitThread crt = new ConsRabbitThread(nMsg,topic);
    crt.run();
  }

  public static ArrayList<Thread> produceT(int nMsg, String topic, int nProd) {
    ArrayList<Thread> alProd = new ArrayList();
    int msgPerProd = nMsg / nProd;
    for (int i = 0; i < nProd; i++) {
      alProd.add(new Thread(new ProdRabbitThread(msgPerProd, topic)));
      alProd.get(i).start();
    }
    return alProd;
  }

  public static ArrayList<Thread> consumeT(int nMsg, String topic, int nCons) {
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
}

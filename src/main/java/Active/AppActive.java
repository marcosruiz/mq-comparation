package Active;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;

public class AppActive {

  public static void main(String[] args) throws Exception {
    int nMsg = 1000000;
    String topic = "TEST.FOO";
//    mainSecuential(nMsg, topic);
    mainMultiThread(nMsg, topic, 10, 10);
  }

  /**
   * Producimos y consumimos con un hilo por productor y por consumidor
   * @param nMsg
   * @param topic
   */
  public static void mainMultiThread(int nMsg, String topic, int nProd, int nCons) throws JMSException, InterruptedException {
    // START PRODUCERS
    System.out.println("PRODUCTOR");
    long timeStart = System.currentTimeMillis();
    ArrayList<Thread> alProd = produceT(nMsg, nProd, topic);
    // Wait to end
    for (int i = 0; i < nProd; i++) {
      alProd.get(i).join();
    }
    printStats(timeStart, nMsg);

    // START CONSUMERS
    System.out.println("CONSUMIDOR");
    timeStart = System.currentTimeMillis();
    ArrayList<Thread> alCons = consumeT(nMsg, nCons, topic);
    //Wait to end
    for (int i = 0; i < nCons; i++) {
      alCons.get(i).join();
    }
    printStats(timeStart, nMsg);
  }


  /**
   * Extreae nMsg mensajes del topic especificado en nCons hilos
   *
   * @param nMsg
   * @param nCons
   * @param topic
   * @return
   */
  public static ArrayList<Thread> consumeT(int nMsg, int nCons, String topic) {
    ArrayList<Thread> alCons = new ArrayList();
    int msgPerCons = nMsg / nCons;
    for (int i = 0; i < nCons; i++) {
      alCons.add(new Thread(new ConsActiveThread(msgPerCons, topic)));
    }
    for (int i = 0; i < nCons; i++) {
      alCons.get(i).start();
    }
    return alCons;
  }

  /**
   * Inserta nMsg mensajes en el topic especificado en nProd hilos
   *
   * @param nMsg
   * @param nProd
   * @param topic
   * @return
   */
  public static ArrayList<Thread> produceT(int nMsg, int nProd, String topic) {
    ArrayList<Thread> alProd = new ArrayList();
    int msgPerProd = nMsg / nProd;
    for (int i = 0; i < nProd; i++) {
      alProd.add(new Thread(new ProdActiveThread(msgPerProd, topic)));
      alProd.get(i).start();
    }
    return alProd;
  }

  /**
   * Producimos y consumimos
   * @param nMsg
   * @param topic
   * @throws JMSException
   */
  public static void mainSecuential(int nMsg, String topic) throws JMSException {
    //PRODUCTOR
    System.out.println("PRODUCTOR");
    long timeStart = System.currentTimeMillis();
    produce(nMsg, topic);
    printStats(timeStart, nMsg);

    // CONSUMIDOR
    System.out.println("CONSUMIDOR");
    timeStart = System.currentTimeMillis();
    consume(nMsg, topic);
    printStats(timeStart, nMsg);
  }

  /**
   * Muestra por la salida estandar algunos stats
   * @param timeStart
   * @param nMsg
   */
  private static void printStats(long timeStart, int nMsg) {
    long totalMem = Runtime.getRuntime().totalMemory();
    long freeMem = Runtime.getRuntime().freeMemory();
    long usedMem = totalMem-freeMem;
    long maxMem = Runtime.getRuntime().maxMemory();
    long timeEnd = System.currentTimeMillis();
    double diffMillis = timeEnd - timeStart;
    double diffSec = diffMillis / 1000;
    double vel = nMsg / diffSec;
    System.out.println("Tiempo total: " + diffSec + " seconds");
    System.out.println("Velocidad: " + vel + " msg/seg");
    System.out.println("Memory used: " + usedMem);
    System.out.println("Memory max: " + maxMem);
  }

  public static void thread(Runnable runnable, boolean daemon) {
    Thread brokerThread = new Thread(runnable);
    brokerThread.setDaemon(daemon);
    brokerThread.start();
  }

  public static void produce(int nMsg, String topic) {
    ProdActiveThread pat = new ProdActiveThread(nMsg, topic);
    pat.run();
  }

  public static void consume(int nMsg, String topic) {
    ConsActiveThread cat = new ConsActiveThread(nMsg, topic);
    cat.run();
  }
}

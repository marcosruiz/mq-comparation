package Active;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;

public class AppActive {

  public static void main(String[] args) throws Exception {
    int nMsg = 20;
    String topic = "TEST.FOO";
//    mainSecuential(nMsg, topic);
        mainMultiThread(nMsg, topic, 1, 10);
  }

  /**
   * Producimos y consumimos con un consumidor y un productor
   * @param nMsg
   * @param topic
   * @throws JMSException
   */
  public static void mainSecuential(int nMsg, String topic) throws JMSException {
    // Create a ConnectionFactory
    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
    // Create a Connection
    Connection connection = connectionFactory.createConnection();
    connection.start();
    // Create a Session
    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

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

    session.close();
    connection.close();
  }

  /**
   * Producimos y consumimos lanzando un hilo por cada productor y cada consumidor
   * @param nMsg
   * @param topic
   */
  public static void mainMultiThread(int nMsg, String topic, int nProd, int nCons) throws JMSException, InterruptedException {
    // Create a ConnectionFactory
    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
    // Create a Connection
    Connection connection = connectionFactory.createConnection();
    connection.start();
    // Create a Session
    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

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

    session.close();
    connection.close();
  }


  /**
   * Devuelve nCons hilos los cuales han sido lanzados. En total consumirán nMsg en el topic especificado.
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
   * Devuelve nProd hilos los cuales han sido lanzados. En total producirán nMsg en el topic especificado.
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
   * Muestra por la salida estandar algunos stats
   * @param timeStart
   * @param nMsg
   */
  private static void printStats(long timeStart, int nMsg) {
    // Tiempo y velocidad
    long timeEnd = System.currentTimeMillis();
    double diffMillis = timeEnd - timeStart;
    double diffSec = diffMillis / 1000;
    double vel = nMsg / diffSec;
    System.out.println("Tiempo total: " + diffSec + " seconds");
    System.out.println("Velocidad: " + vel + " msg/seg");

    // Memoria RAM
    long totalMem = Runtime.getRuntime().totalMemory();
    long freeMem = Runtime.getRuntime().freeMemory();
    long usedMem = totalMem-freeMem;
    long maxMem = Runtime.getRuntime().maxMemory();
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

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

    // Clean up
    session.close();
    connection.close();
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
   * Producimos y consumimos con un hilo por productor y por consumidor
   * @param nMsg
   * @param topic
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
    produce(nMsg, session, topic);
    printStats(timeStart, nMsg);

    // CONSUMIDOR
    System.out.println("CONSUMIDOR");
    timeStart = System.currentTimeMillis();
    consume(nMsg, session, topic);
    printStats(timeStart, nMsg);

    // Clean up
    session.close();
    connection.close();
  }

  /**
   * Muestra por la salida estandar algunos stats
   * @param timeStart
   * @param nMsg
   */
  private static void printStats(long timeStart, int nMsg) {
    long timeEnd = System.currentTimeMillis();
    double diffMillis = timeEnd - timeStart;
    double diffSec = diffMillis / 1000;
    double vel = nMsg / diffSec;
    System.out.println("Tiempo total: " + diffSec + " seconds");
    System.out.println("Velocidad: " + vel + " msg/seg");
  }



  public static void thread(Runnable runnable, boolean daemon) {
    Thread brokerThread = new Thread(runnable);
    brokerThread.setDaemon(daemon);
    brokerThread.start();
  }

  public static void produce(int messages, Session session, String topic) {
    try {

      // Create the destination (Topic or Queue)
      Destination destination = session.createQueue(topic);

      // Create a MessageProducer from the Session to the Topic or Queue
      MessageProducer producer = session.createProducer(destination);
      producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

      // Send n messages: each message has a number as content
      for (int i = 0; i < messages; i++) {
        TextMessage message = session.createTextMessage(Integer.toString(i));
        producer.send(message);
        System.out.print("\r" + ((i + 1) * 100 / messages) + "%");
      }
      System.out.println("");

    } catch (Exception e) {
      System.out.println("Caught: " + e);
      e.printStackTrace();
    }
  }


  public static void consume(int messages, Session session, String topic) {
    try {

      // Create the destination (Topic or Queue)
      Destination destination = session.createQueue(topic);

      // Create a MessageConsumer from the Session to the Topic or Queue
      MessageConsumer consumer = session.createConsumer(destination);

      // Wait for n messages
      for (int i = 0; i < messages; i++) {
        Message message = consumer.receive(1000);
        if (message instanceof TextMessage) {
          TextMessage textMessage = (TextMessage) message;
          String text = textMessage.getText();
          //          System.out.println("Received: " + text);
          //          System.out.print("\r" + ((i+1)*100/messages) + "% : Received: " + text);
        } else {
          System.out.println("Received: " + message);
        }
      }
      //      System.out.println();

      consumer.close();
    } catch (Exception e) {
      System.out.println("Caught: " + e);
      e.printStackTrace();
    }
  }
}

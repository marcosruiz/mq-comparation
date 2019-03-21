package mq.active;

import mq.MQInt;
import mq.MQMTInt;
import mq.MQStats;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import java.util.ArrayList;

public class MQActive implements MQInt, MQMTInt{

  @Override
  public void produce(int nMsg, String topic) {
    ProdActiveThread pat = new ProdActiveThread(nMsg, topic);
    pat.run();
  }

  @Override
  public void consume(int nMsg, String topic) {
    ConsActiveThread cat = new ConsActiveThread(nMsg, topic);
    cat.run();
  }

  @Override
  public void produceAndConsume(int nMsg, String topic) throws JMSException {
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
    this.produce(nMsg, topic);
    MQStats.printStats(timeStart, nMsg);

    // CONSUMIDOR
    System.out.println("CONSUMIDOR");
    timeStart = System.currentTimeMillis();
    consume(nMsg, topic);
    MQStats.printStats(timeStart, nMsg);

    session.close();
    connection.close();
  }

  @Override
  public ArrayList<Thread> produceMT(int nMsg, int nProd, String topic) {
    ArrayList<Thread> alProd = new ArrayList();
    int msgPerProd = nMsg / nProd;
    for (int i = 0; i < nProd; i++) {
      alProd.add(new Thread(new ProdActiveThread(msgPerProd, topic)));
      alProd.get(i).start();
    }
    return alProd;
  }

  @Override
  public ArrayList<Thread> consumeMT(int nMsg, int nCons, String topic) {
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

  @Override
  public void produceAndConsumeMT(int nMsg, String topic, int nProd, int nCons)
      throws JMSException, InterruptedException {
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
    ArrayList<Thread> alProd = this.produceMT(nMsg, nProd, topic);
    // Wait to end
    for (int i = 0; i < nProd; i++) {
      alProd.get(i).join();
    }
    MQStats.printStats(timeStart, nMsg);

    // START CONSUMERS
    System.out.println("CONSUMIDOR");
    timeStart = System.currentTimeMillis();
    ArrayList<Thread> alCons = this.consumeMT(nMsg, nCons, topic);
    //Wait to end
    for (int i = 0; i < nCons; i++) {
      alCons.get(i).join();
    }
    MQStats.printStats(timeStart, nMsg);

    session.close();
    connection.close();
  }
}

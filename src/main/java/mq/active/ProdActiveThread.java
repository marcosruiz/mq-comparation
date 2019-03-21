package mq.active;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ProdActiveThread implements Runnable {

  private final int nMsg;
  private final String topic;

  public ProdActiveThread(int nMsg, String topic) {
    this.topic = topic;
    this.nMsg = nMsg;
  }

  public void run() {
    try {
      // Create a ConnectionFactory
      ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

      // Create a Connection
      Connection connection = connectionFactory.createConnection();
      connection.start();

      // Create a Session
      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

      // Create the destination (Topic or Queue)
      Destination destination = session.createQueue(topic);

      // Create a MessageProducer from the Session to the Topic or Queue
      MessageProducer producer = session.createProducer(destination);
      producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

      for (int i = 0; i < nMsg; i++) {
        // Create a messages
        String text = Integer.toString(i);
        TextMessage message = session.createTextMessage(text);
        // Tell the producer to send the message
        producer.send(message);
//        System.out.println("Sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());
        System.out.print("\r" + (i+1) + "/" + nMsg);
      }
      System.out.println();

      // Clean up
      producer.close();
      session.close();
      connection.close();
    } catch (Exception e) {
      System.out.println("Caught: " + e);
      e.printStackTrace();
    }
  }
}

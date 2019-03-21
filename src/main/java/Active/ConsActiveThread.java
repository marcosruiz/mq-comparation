package Active;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ConsActiveThread implements Runnable, ExceptionListener {

  private final String topic;
  private final int nMsg;

  public ConsActiveThread(int nMsg, String topic) {
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

      connection.setExceptionListener(this);

      // Create a Session
      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

      // Create the destination (Topic or Queue)
      Destination destination = session.createQueue(topic);

      // Create a MessageConsumer from the Session to the Topic or Queue
      MessageConsumer consumer = session.createConsumer(destination);

      int i = 0;
      while (i < nMsg) {
        // Wait for a message
        Message message = consumer.receive(1000); // 1000 milliseconds

        if (message instanceof TextMessage) {
          TextMessage textMessage = (TextMessage) message;
          String text = textMessage.getText();
          i++;
//          System.out.println("Received: " + text);
          System.out.print("\r" + i + "/" + nMsg);
        } else {
          System.out.println("Received: " + message);
        }
      }
      System.out.println();
      consumer.close();
      session.close();
      connection.close();
    } catch (Exception e) {
      System.out.println("Caught: " + e);
      e.printStackTrace();
    }
  }

  public synchronized void onException(JMSException ex) {
    System.out.println("JMS Exception occured.  Shutting down client.");
  }
}

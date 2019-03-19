package Active;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class AppActive {
  final static int TOTAL_ITER = 600000;

  public static void main(String[] args) throws Exception{
    // Create a ConnectionFactory
    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
    // Create a Connection
    Connection connection = connectionFactory.createConnection();
    connection.start();
    // Create a Session
    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

    //PRODUCTOR
    System.out.println("PRODUCTOR");
    long timeStart= System.currentTimeMillis();
    produce(TOTAL_ITER, session);

    long timeEnd=System.currentTimeMillis();
    double diffMillis = timeEnd - timeStart;
    double diffSec = diffMillis/1000;
    System.out.println("Velocidad: " + TOTAL_ITER/diffSec + " msg/seg");

    // CONSUMIDOR
    System.out.println("CONSUMIDOR");
    timeStart = System.currentTimeMillis();
    consume(TOTAL_ITER, session);
    timeEnd=System.currentTimeMillis();
    diffMillis = timeEnd - timeStart;
    diffSec = diffMillis/1000;
    System.out.println("Velocidad: " + TOTAL_ITER/diffSec + " msg/seg");

    // Clean up
    session.close();
    connection.close();
  }

  public static void mainThread(){
        thread(new HelloWorldProducer(), false);
        thread(new HelloWorldProducer(), false);
        thread(new HelloWorldConsumer(), false);
        thread(new HelloWorldConsumer(), false);
  }


  public static void thread(Runnable runnable, boolean daemon) {
    Thread brokerThread = new Thread(runnable);
    brokerThread.setDaemon(daemon);
    brokerThread.start();
  }

  public static void produce(int messages, Session session) {
    try {

      // Create the destination (Topic or Queue)
      Destination destination = session.createQueue("TEST.FOO");

      // Create a MessageProducer from the Session to the Topic or Queue
      MessageProducer producer = session.createProducer(destination);
      producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

      // Send n messages: each message has a number as content
      for (int i = 0; i < messages; i++) {
        TextMessage message = session.createTextMessage(Integer.toString(i));
        producer.send(message);
        System.out.print("\r" + ((i+1)*100/messages) + "%");
      }
      System.out.println("");

    } catch (Exception e) {
      System.out.println("Caught: " + e);
      e.printStackTrace();
    }
  }


  public static void consume(int messages, Session session) {
    try {

      // Create the destination (Topic or Queue)
      Destination destination = session.createQueue("TEST.FOO");

      // Create a MessageConsumer from the Session to the Topic or Queue
      MessageConsumer consumer = session.createConsumer(destination);

      // Wait for n messages
      for (int i = 0; i < messages; i++) {
        Message message = consumer.receive(1000);
        if (message instanceof TextMessage) {
          TextMessage textMessage = (TextMessage) message;
          String text = textMessage.getText();
//          System.out.println("Received: " + text);
          System.out.print("\r" + ((i+1)*100/messages) + "% : Received: " + text);
        } else {
          System.out.println("Received: " + message);
        }
      }
      System.out.println();

      consumer.close();
    } catch (Exception e) {
      System.out.println("Caught: " + e);
      e.printStackTrace();
    }
  }

  public static class HelloWorldProducer implements Runnable {
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
        Destination destination = session.createQueue("TEST.FOO");

        // Create a MessageProducer from the Session to the Topic or Queue
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        // Create a messages
        String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
        TextMessage message = session.createTextMessage(text);

        // Tell the producer to send the message
        System.out.println("Sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());
        producer.send(message);

        // Clean up
        session.close();
        connection.close();
      } catch (Exception e) {
        System.out.println("Caught: " + e);
        e.printStackTrace();
      }
    }
  }


  public static class HelloWorldConsumer implements Runnable, ExceptionListener {
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
        Destination destination = session.createQueue("TEST.FOO");

        // Create a MessageConsumer from the Session to the Topic or Queue
        MessageConsumer consumer = session.createConsumer(destination);

        // Wait for a message
        Message message = consumer.receive(1000);


        if (message instanceof TextMessage) {
          TextMessage textMessage = (TextMessage) message;
          String text = textMessage.getText();
          System.out.println("Received: " + text);
        } else {
          System.out.println("Received: " + message);
        }

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
}

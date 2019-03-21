package mq.kafka;

import mq.MQInt;
import mq.MQMTInt;
import mq.MQStats;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.Properties;

public class MQKafka implements MQInt, MQMTInt {

  @Override
  public void produceAndConsume(int nMsg, String topic) {
    //Producer
    System.out.println("\nPRODUCERS");
    long timeStart = System.currentTimeMillis();
    produce(nMsg, topic);
    MQStats.printStats(timeStart, nMsg);

    // Consumer
    System.out.println("CONSUMERS");
    timeStart = System.currentTimeMillis();
    consume(nMsg, topic);
    MQStats.printStats(timeStart, nMsg);
  }

  @Override
  public void produce(int nMsg, String topic) {
    new ProdKafkaThread(getPropsProd(), nMsg, topic).run();
  }

  @Override
  public void consume(int nMsg, String topic) {
    new ConsKafkaThread(getPropsCons(), nMsg, topic).run();
  }

  @Override
  public void produceAndConsumeMT(int nMsg, String topic, int nProd, int nCons)
      throws JMSException, InterruptedException {
    // Control producers
    System.out.println("PRODUCERS");
    long timeStart = System.currentTimeMillis();

    // START PRODUCERS
    ArrayList<Thread> alProd = produceMT(nMsg, nProd, topic);

    // Wait to end
    for (int i = 0; i < nProd; i++) {
      alProd.get(i).join();
    }

    MQStats.printStats(timeStart, nMsg);

    // Control consumers
    System.out.println("CONSUMERS");
    timeStart = System.currentTimeMillis();

    // START CONSUMERS
    ArrayList<Thread> alCons = consumeMT(nMsg, nCons, topic);

    //Wait to end
    for (int i = 0; i < nCons; i++) {
      alCons.get(i).join();
    }

    MQStats.printStats(timeStart, nMsg);
  }

  @Override
  public ArrayList<Thread> produceMT(int nMsg, int nProd, String topic) {
    ArrayList<Thread> alProd = new ArrayList();
    int msgPerProd = nMsg / nProd;
    for (int i = 0; i < nProd; i++) {
      alProd.add(new Thread(new ProdKafkaThread(getPropsProd(), msgPerProd, topic)));
      alProd.get(i).start();
    }
    return alProd;
  }

  @Override
  public ArrayList<Thread> consumeMT(int nMsg, int nCons, String topic) {
    ArrayList<Thread> alCons = new ArrayList();
    int msgPerCons = nMsg / nCons;
    for (int i = 0; i < nCons; i++) {
      alCons.add(new Thread(new ConsKafkaThread(getPropsCons(), msgPerCons, topic)));
    }

    for (int i = 0; i < nCons; i++) {
      alCons.get(i).start();
    }
    return alCons;
  }

  /**
   * Devuelve las propiedades para un Productor
   *
   * @return
   */
  public static Properties getPropsProd() {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094,localhost:9095");
    props.put("acks", "1");
    props.put("delivery.timeout.ms", 30000);
    props.put("batch.size", 8196);
    props.put("linger.ms", 1);
    props.put("request.timeout.ms", 15000);
    props.put("buffer.memory", 67108864);
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("compression.type", "none");
    return props;
  }

  /**
   * Devuelve las propiedades para un Consumidor
   *
   * @return
   */
  public static Properties getPropsCons() {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094,localhost:9095");
    props.put("group.id", "test");
    props.put("enable.auto.commit", "true");
    props.put("auto.commit.interval.ms", "1000");
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    return props;
  }
}

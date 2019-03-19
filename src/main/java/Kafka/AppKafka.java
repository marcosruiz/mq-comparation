package Kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

/**
 * Clase main para sacar metricas de Kafka
 */
public class AppKafka {

  /**
   * Producimos y recibimos 1 millon de mensajes en test_1
   *
   * @param args
   */
  public static void main(String[] args) {
    int nMsg = 1000000;
    String topic = "test_1";

    // SECUENTIAL
    mainSecuential(nMsg, topic);

    // MULTITHREAD
    try {
      mainMultiThread(nMsg, topic, 1, 10);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Producimos y consumimos en secuencial nMsg mensajes en el topic especificado
   */
  public static void mainSecuential(int nMsg, String topic) {
    //Producer
    System.out.println("PRODUCTOR");
    long timeStart = System.currentTimeMillis();
    produce(nMsg, topic);
    printStats(nMsg, timeStart);

    // Consumer
    System.out.println("CONSUMIDOR");
    timeStart = System.currentTimeMillis();
    consume(nMsg, topic);
    printStats(nMsg, timeStart);
  }

  private static void printStats(int nMsg, long timeStart) {
    long timeEnd = System.currentTimeMillis();
    double millis = (timeEnd - timeStart);
    double vel = nMsg / (millis / 1000);
    System.out.println("Tiempo total: " + (timeEnd - timeStart) + " milliseconds");
    System.out.println("Velocidad: " + vel + " msg/seg");
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
      alProd.add(new Thread(new ProdKafkaThread(getPropsProd(), msgPerProd, topic)));
      alProd.get(i).start();
    }
    return alProd;
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
      alCons.add(new Thread(new ConsKafkaThread(getPropsCons(), msgPerCons, topic)));
    }

    for (int i = 0; i < nCons; i++) {
      alCons.get(i).start();
    }
    return alCons;
  }

  /**
   * Producimos y consumimos con un hilo por productor y por consumidor
   *
   * @param nMsg
   * @param topic
   * @param numProd
   * @param numCons
   * @throws InterruptedException
   */
  public static void mainMultiThread(int nMsg, String topic, int numProd, int numCons) throws InterruptedException {
    // Control producers
    System.out.println("PRODUCERS");
    long timeStart = System.currentTimeMillis();

    // START PRODUCERS
    ArrayList<Thread> alProd = produceT(nMsg, numProd, topic);

    // Wait to end
    for (int i = 0; i < numProd; i++) {
      alProd.get(i).join();
    }

    printStats(nMsg, timeStart);

    // Control consumers
    System.out.println("CONSUMERS");
    timeStart = System.currentTimeMillis();

    // START CONSUMERS
    ArrayList<Thread> alCons = consumeT(nMsg, numCons, topic);

    //Wait to end
    for (int i = 0; i < numCons; i++) {
      alCons.get(i).join();
    }

    printStats(nMsg, timeStart);
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

  /**
   * Envia nMsg mensajes al topic especificado
   *
   * @param nMsg
   * @param topic
   */
  public static void produce(int nMsg, String topic) {
    Producer<String, String> producer = new KafkaProducer<>(getPropsProd());
    for (int i = 0; i < nMsg; i++) {
      producer.send(new ProducerRecord<String, String>(topic, Integer.toString(i), Integer.toString(i)));
    }
    producer.close();
  }

  /**
   * Extrae nMsg mensajes del topic especificado
   *
   * @param nMsg
   * @param topic
   */
  public static void consume(int nMsg, String topic) {
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(getPropsCons());
    consumer.subscribe(Arrays.asList(topic));
    long timeStart = System.currentTimeMillis();
    int i = 0;
    while (i < nMsg) {
      ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
      for (ConsumerRecord<String, String> record : records) {
        // System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
        i++;
        // double percentage = (i / nMsg) * 100;
        // System.out.print("\rConsumiendo: " + percentage + " %");
      }
    }
    consumer.unsubscribe();
    // System.out.println("");
  }
}

package Kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class ProdKafkaThread implements Runnable{

  private final String topic;
  private final Properties props;
  private final int nMsg;

  public ProdKafkaThread(Properties props, int nMsg, String topic){
    this.props = props;
    this.nMsg = nMsg;
    this.topic = topic;
  }
  @Override
  public void run() {
    Producer<String, String> producer = new KafkaProducer<>(props);
    for (int i = 0; i < nMsg; i++) {
      producer.send(new ProducerRecord<String, String>(topic, Integer.toString(i), Integer.toString(i)));
    }
    producer.close();
  }
}

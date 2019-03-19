package Kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsKafkaThread implements Runnable {

  private final String topic;
  private final Properties props;
  private final int nMsg;

  public ConsKafkaThread(Properties props, int nMsg, String topic){
    this.props = props;
    this.nMsg = nMsg;
    this.topic = topic;
  }
  @Override
  public void run() {
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(Arrays.asList(topic));
    int i = 0;
    while (i < nMsg) {
      ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
      for (ConsumerRecord<String, String> record : records) {
//        System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
        i++;
      }
    }
    consumer.unsubscribe();
  }
}

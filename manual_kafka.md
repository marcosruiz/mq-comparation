# Manual de Kafka

Para que los comandos funcionen debes estar en el directorio raiz de Kafka

## Comandos útiles
- Iniciar Zookeeper server

`bin/zookeeper-server-start.sh config/zookeeper.properties`

- Parar Zookeper server (sin probar)

`bin/zookeeper-server-stop.sh`

- Iniciar Kafka server

`bin/kafka-server-start.sh config/server.properties`

## Topics

- Crear un 'topic'

`bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test`

- Eliminar un 'topic'

`bin/kafka-topics.sh --delete --zookeeper localhost:2181 --topic test`

- Describir topic

`bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic test`

- Listar 'topics'

`bin/kafka-topics.sh --list --zookeeper localhost:2181`

- Editar 'topic': Añadir partitions

`bin/kafka-topics.sh --alter --zookeeper localhost:2181 --topic test --partitions 10`

- Editar 'topic': Editar replication

`bin/kafka-reassign-partitions.sh --zookeeper localhost:2181 --reassignment-json-file edit-replication-factor.json --execute`

- Ver consumer group

`bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group test`

`bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --state --group test`

**edit-replication-factor.json**
```
{
 "version":1,
 "partitions":[
      {"topic":"test","partition":0,"replicas":[0,1]}
 ]
}
```

- Productor interactivo

`bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test`

- Consumidor

`bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning`

`bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test_1 --from-beginning`

- Productor y Consumidor: Por cada linea de test_1.txt envia un mensaje a la cola indicada en connect-file-source.properties y lo deja en test_1.sink.txt

`bin/connect-standalone.sh config/connect-standalone.properties config/connect-file-source.properties config/connect-file-sink.properties`

- Consumidor

`bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic connect-test --from-beginning`

`bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test_1 --from-beginning`

[Kafka Quickstart](http://kafka.apache.org/quickstart)
[Kafka ejemplo Java WordCountDemo](http://kafka.apache.org/21/documentation/streams/quickstart)

## Testing

Productor envia 15M de mensajes
```
bin/kafka-producer-perf-test.sh --topic test \
--num-records 15000000 \
--record-size 100 \
--throughput 15000000 \
--producer-props \
acks=1 \
bootstrap.servers=localhost:9092 \
buffer.memory=67108864 \
compression.type=none \
batch.size=8196
```

Consumidor recibe 15M de mensajes
```
bin/kafka-consumer-perf-test.sh --topic test \
--broker-list localhost:9092 \
--messages 15000000 \
--threads 1
```

## Ejemplo de WordCount

```
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic streams-plaintext-input

bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic streams-wordcount-processor-output

bin/kafka-console-producer.sh --broker-list localhost:9092 --topic streams-plaintext-input

bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic streams-wordcount-processor-output --from-beginning
```

```
bin/kafka-topics.sh --alter --zookeeper localhost:2181 --partitions 10 --topic demo
```

## Enlaces de interés
[Tutorial consumidores competitivos](https://dzone.com/articles/kafka-producer-and-consumer-example)

[Repositorio del Tutorial consumidores competitivos](https://github.com/garg-geek/kafka)

[Problema con los lambda](https://stackoverflow.com/questions/32923586/maven-lambda-expressions-are-not-supported-in-source-1-5/32923706#32923706)

[Introducción a Kafka, Flume, Spark, Storm](https://blog.gfi.es/flume-kafka-spark-y-storm-un-nuevo-ejercito-apache/)

[Algunos atajos comandos útiles para Kafka](https://gist.github.com/ursuad/e5b8542024a15e4db601f34906b30bb5)

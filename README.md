# MQ-Comparation

MQ-Comparation es un proyecto que busca comparar los 3 servicios de colas de mensajes más populares del mercado: Kafka, RabbitMQ y ActiveMQ

## Kafka

### Primeros pasos

- Instalar Kafka en tu computador
- Instalar maven
- Ejecutar en la raiz del directorio del proyecto `mvn clean install`
- Iniciar servidor zookeeper
- Iniciar servidores kafka
- Crear un topic llamado 'test_1'

#### Script para iniciar Zookeeper, Kafka y la cola

En la raiz del directorio de kafka

```
bin/zookeeper-server-start.sh config/zookeeper.properties &
sleep 5
bin/kafka-server-start.sh config/server.properties &
sleep 2
bin/kafka-server-start.sh config/server-1.properties &
sleep 2
bin/kafka-server-start.sh config/server-2.properties &
sleep 2
bin/kafka-server-start.sh config/server-3.properties &
sleep 2
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 3 --partitions 10 --topic test_1
```

Importante: Si no creas tú la cola se creará una automáticamente con una partición y con 1 de factor de replicación, es decir, una cola equivalente a ejecutar esta linea: `bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test_1`

## ActiveMQ

## RabbitMQ


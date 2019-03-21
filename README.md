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

Copia el contenido de `$PROJECT_HOME/src/main/resources/kafka_scripts` en `$KAFKA_HOME` para posteriormente ejecutar `start_zookeeper_kafka.sh` que desplegará 1 servidor zookeeper, 4 servidores Kafka y una cola llamada test_1.

## ActiveMQ

ActiveMQ se lanza desde el propio código por lo que no es necesario ejecutar ninguna instrucción en el terminal.

## RabbitMQ

RabbitMQ se lanza desde el propio código por lo que no es necesario ejecutar ninguna instrucción en el terminal.

### Configurar colas



## Conclusiones

### Velocidad

### CPU

### Memoria RAM


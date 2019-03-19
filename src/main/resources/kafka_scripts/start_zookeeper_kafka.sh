# Nota: Tanto zookeeper como kafka deben estar en marcha
# Nos tomará al menos 13 segundos


echo "">zookeeper.out
echo "">server.output
echo "">server-1.out
echo "">server-2.out
echo "">server-3.out

bin/zookeeper-server-start.sh config/zookeeper.properties >> zookeeper.out &
sleep 5
bin/kafka-server-start.sh config/server.properties >> server.out &
sleep 2
bin/kafka-server-start.sh config/server-1.properties >> server-1.out &
sleep 2
bin/kafka-server-start.sh config/server-2.properties >> server-2.out &
sleep 2
bin/kafka-server-start.sh config/server-3.properties >> server-3.out &
sleep 2
# bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test_1
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 3 --partitions 10 --topic test_1

# Productor
# bin/connect-standalone.sh config/connect-standalone.properties config/connect-file-source.properties config/connect-file-sink.properties
# Consumidor
# bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test_1 --from-beginning

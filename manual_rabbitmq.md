# Manual RabbitMQ

## Command Line Tool

Primero hay que instalar RabbitMQ en el sistema

Habilitamos rabbitmq_management
```
rabbitmq-plugins enable rabbitmq_management

```

Descargamos rabbitmqadmin, lo copiamos a `/usr/local/bin`, le damos permisos de ejecución y comprobamos que está en el PATH
```
http://localhost:15672/cli/rabbitmqadmin
cp ~/Descargas/rabbitmqadmin /usr/local/bin/
chmod u+x /usr/local/bin/rabbitmqadmin
rabbitmqadmin --help
```

Listar colas de mansajes
```
rabbitmqadmin -V test list exchanges
```

Crear cola
```
rabbitmqadmin declare queue name=my-new-queue durable=false
```

Publicar mensaje
```
rabbitmqadmin publish exchange=amq.default routing_key=test payload="hello, world"
```

Para el fichero de configuración llamado `rabbitmq.conf` se copia en `/etc/rabbitmq`
```
cp ~/Descargas/rabbitmq.conf /etc/rabbitmq/
```

```
//toCopyAndPaste
```

## Cluster
[RabbitMQ Tutorial Cluster en Español](https://magmax.org/blog/colas-de-mensajes-rabbitmq/)

Estatus
```
sudo rabbitmqctl cluster_status
sudo rabbitmqctl stop_app
sudo rabbitmqctl join_cluster rabbit@jarvis
sudo rabbitmqctl start_app
sudo rabbitmqctl cluster_status
```




[Información sobre el fichero de configuración](https://www.rabbitmq.com/configure.html)

[Todos los tutoriales](https://www.rabbitmq.com/getstarted.html)

[Tutoriales: Work Queues](https://www.rabbitmq.com/tutorials/tutorial-two-java.html)

#Mata ZOOKEPER y todos los servidores de KAFKA
ps -ef | grep 'kafka' | grep -v grep | awk '{print $2}' | xargs kill -9

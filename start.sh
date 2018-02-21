#! /bin/bash
echo "Starting MongoDB"
service mongod start

echo "Starting nginx"
service nginx start
service nginx stop
service nginx start

echo "Starting Solr"
./deployment/solr.sh start

echo "Starting ABSTAT"
cd summarization-spring
mvn package
java -jar target/summarization-spring-0.0.1-SNAPSHOT.jar
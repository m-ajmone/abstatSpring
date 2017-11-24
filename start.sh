#! /bin/bash
echo "Starting MongoDB"
service mongod start

echo "Starting nginx"
service nginx start

echo "Starting Solr"
./deployment/solr.sh start

echo "Starting ABSTAT"
cd summarization-spring
mvn spring-boot:run@abstat
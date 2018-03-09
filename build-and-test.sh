#! /bin/bash

function unit_test(){
	echo
	echo "Unit Test ###########################"
	echo
	mvn --quiet -Dtest=UnitTests test
}


function system_test(){
	echo
	echo "System Test ###########################"
	echo
	mvn --quiet -Dtest=SystemTests test
}


function build(){
	echo
	echo "Build ###########################"
	echo
	mvn clean --quiet package -Dmaven.test.skip=true  -Dstart-class=com.start.abstat.AbstatApplication4Test
}


function summarize(){
	echo
 	echo "Summarize ###########################"
 	echo
	java -Xmx32000m -Xms256m -jar target/summarization-spring-0.0.1-SNAPSHOT.jar
}


function initMongo(){
    now=$(date +"%x %X")

	mongo_command="db.datasetAndOntology.find({_id:\"system-test_dataset\"}).limit(1).size();"
	exists="$(mongo 127.0.0.1/abstat --eval $mongo_command )"
	if [[ "$exists" == *"0" ]]; then
			command="var document = {\"_id\" : \"system-test_dataset\",\"_class\" : \"com.model.Dataset\",\"name\" : \"system-test\",\"path\" : \"../data/DsAndOnt/dataset/system-test/system-test.nt\",\"timestamp\" : \""$now"\",\"type\" : \"dataset\",\"split\" : false};db.datasetAndOntology.insert(document);"
			mongo 127.0.0.1/abstat --quiet --eval "$command"
	fi

	mongo_command="db.datasetAndOntology.find({_id:\"system-test_ontology\"}).limit(1).size();"
	exists="$(mongo 127.0.0.1/abstat --eval $mongo_command )"
	if [[ "$exists" == *"0" ]]; then
			command="var document = {\"_id\" : \"system-test_ontology\",\"_class\" : \"com.model.Ontology\",\"name\" : \"dbpedia_2014\",\"path\" : \"../data/DsAndOnt/ontology/dbpedia_2014.owl\",\"timestamp\" : \""$now"\",\"type\" : \"ontology\"};db.datasetAndOntology.insert(document);"
			mongo 127.0.0.1/abstat --quiet --eval "$command"
	fi


	mongo_command="db.datasetAndOntology.find({_id:\"empty_ontology\"}).limit(1).size();"
	exists="$(mongo 127.0.0.1/abstat --eval $mongo_command )"
	if [[ "$exists" == *"0" ]]; then
			command="var document = {\"_id\" : \"empty_ontology\",\"_class\" : \"com.model.Ontology\",\"name\" : \"no ontology\",\"path\" : \"../data/DsAndOnt/ontology/emptyOnt.owl\",\"timestamp\" : \""$now"\",\"type\" : \"ontology\"};db.datasetAndOntology.insert(document);"
			mongo 127.0.0.1/abstat --quiet --eval "$command"
	fi
}


cd summarization-spring
initMongo
unit_test
build
summarize
#system_test
cd ..
./abstat.sh build

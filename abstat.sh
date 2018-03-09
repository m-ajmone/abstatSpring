#! /bin/bash

function start(){
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
	java -Xmx32000m -Xms256m  -jar target/summarization-spring-0.0.1-SNAPSHOT.jar
}


function build(){
	cd summarization-spring
	mvn package 	
}


function install(){
	sudo apt-get update
	relative_path=`dirname $0`
	root=`cd $relative_path;pwd`

	echo
	echo "Installing nginx -------------------------------------------------------------------------------------"
	sudo apt-get install -y nginx
	baseLog_dir="$root/data/logs/reverse-proxy"
	accesLog_file="$baseLog_dir/access.log"
	errorLog_file="$baseLog_dir/error.log"

	#create log files 
	mkdir -p $baseLog_dir
	touch  $accesLog_file
	touch  $errorLog_file

	cd deployment
	#setting paths on config file
	sed -i "s~root_path~$baseLog_dir~g" nginx-model.conf
	sed -i "s~access.log_path~$accesLog_file~g" nginx-model.conf
	sed -i "s~error.log_path~$errorLog_file~g" nginx-model.conf

	cp -rf nginx-model.conf /etc/nginx/nginx.conf

	#restoring previous labels to avoid git 
	sed -i "s~$baseLog_dir~root_path~g" nginx-model.conf
	sed -i "s~$accesLog_file~access.log_path~g" nginx-model.conf
	sed -i "s~$errorLog_file~error.log_path~g" nginx-model.conf

	cd $root

	echo
	echo "Installing mongodb ----------------------------------------------------------------------------------------"
	sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 0C49F3730359A14518585931BC711F9BA15703C6
	sudo apt-get update
	ubuntu_version=$(lsb_release -a | grep "Release")

	if [[ $ubuntu_version == *"16"* ]]; then
		echo "deb [ arch=amd64,arm64 ] http://repo.mongodb.org/apt/ubuntu xenial/mongodb-org/3.4 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.4.list
	else
		echo "deb [ arch=amd64 ] http://repo.mongodb.org/apt/ubuntu trusty/mongodb-org/3.4 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.4.list
	fi

	sudo apt-get update
	sudo apt-get install -y mongodb-org

	echo
	echo "Installing java----------------------------------------------------------------------------------------"
	sudo add-apt-repository ppa:webupd8team/java
	sudo apt-get update
	sudo apt-get install -y oracle-java8-installer

	echo
	echo "Installing maven ----------------------------------------------------------------------------------------"
	sudo apt-get install -y maven

	echo
	echo "Installing gawk ----------------------------------------------------------------------------------------"
	sudo apt-get install -y gawk
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


case "$1" in
        start)
			start 
            ;;
        initMongo)
			initMongo 
            ;;
		build)
			build
			;;
		install)
			install
			;;
        *)
        	echo "Usage: bookex start | build | install | initMongo"
			;;
esac

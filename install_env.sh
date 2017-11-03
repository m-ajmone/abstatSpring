#! /bin/bash

sudo apt-get update

echo "Installing nginx"
sudo apt-get install -y nginx
sudo cp -rf deployment/nginx-model.conf /etc/nginx/nginx.conf

echo "Installing mongodb"
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 0C49F3730359A14518585931BC711F9BA15703C6
sudo apt-get update
ubuntu_version=$(lsb_release -a | grep Release)

if [[ $ubuntu_version == *"16"* ]]; then
	echo "deb [ arch=amd64,arm64 ] http://repo.mongodb.org/apt/ubuntu xenial/mongodb-org/3.4 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.4.list
else
	echo "deb [ arch=amd64 ] http://repo.mongodb.org/apt/ubuntu trusty/mongodb-org/3.4 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.4.list
fi

sudo apt-get update
sudo apt-get install -y mongodb-org


echo "Installing java"
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install -y oracle-java8-installer


echo "Installing maven"
sudo apt-get install -y maven
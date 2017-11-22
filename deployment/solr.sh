#!/bin/bash

set -e
command=$1
port=$2

solr/bin/solr $command 


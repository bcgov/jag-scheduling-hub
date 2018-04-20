#!/bin/bash

if [[ $1 ]]; then
	# pass non-zero value to preserve intermediate containers (faster rebuilds)
	BUILD_ARGS="$BUILD_ARGS --rm=false"
fi
MONGO_PID=$(docker ps -a -q -f "name=db_client1")
if [[ -z $MONGO_PID ]]; then
	docker run -p 27017:27017 -d --name db_client1 mongo
fi
docker build -t mean-jag-client1 $BUILD_ARGS .

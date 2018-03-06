#!/bin/bash

# docker build -t jag/servicemix .

# not sure if this must run detached -d or not, leaving stdin open as well

docker run -ti --rm \
  --name jag_servicemix \
  -p 1099:1099 \
  -p 8101:8101 \
  -p 8181:8181 \
  -p 61616:61616 \
  -p 36888:36888 \
  -p 44444:44444 \
  -v `pwd`/deploy:/deploy \
  jag/servicemix

#!/bin/bash
docker stop db_client1
docker start db_client1
docker stop jag-client1
docker run \
-p 3030:3000 \
-p 5858:5858 \
-p 35729:35729 \
-v `pwd`/config:/opt/mean.js/config \
-v `pwd`/modules:/opt/mean.js/modules \
-v `pwd`/public:/opt/mean.js/public \
-v `pwd`/uploads:/opt/mean.js/uploads \
-e "MAILER_SERVICE_PROVIDER=gggmail" \
-e "MAILER_FROM=<Email>" \
-e "MAILER_EMAIL_ID=<Email>" \
-e "MAILER_PASSWORD=<Password>" \
-e "NODE_ENV=${NODE_ENV-development}" \
-e "MONGO_SEED=${MONGO_SEED-true}" \
-e "DISABLE_WATCH=${DISABLE_WATCH-}" \
-ti --rm --link db_client1 --name jag-client1 mean-jag-client1 ${@:-bash}


#!/bin/sh

set -e
set -u
set -o pipefail
#set -x

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
cd $dir

cd .. && make network && sh ../vault/startup.sh -c -i \
  && cd ../config-server && mvn clean verify \
  && make build && make run-detached \
  && cd ../keycloak && make rundev \
  && echo "All done"

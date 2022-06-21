#!/bin/bash
# clean.sh 2022-03-13
# Script for cleaning some docker volumes, secrets, networks and containers

for secret in $(docker secret ls -q); do
  docker secret rm "${secret}" 2>&1
done;

docker volume prune -f && docker network prune -f

docker stop $(docker ps -a -q) \
  && docker rm $(docker ps -a -q) \
  && echo "Containers stopped and removed succesfully" \
  && docker ps -a

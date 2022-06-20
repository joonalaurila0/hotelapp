#!/bin/bash
# clean.sh 2022-03-13
# Script for cleaning some docker volumes, secrets, networks and containers

ns=hotelapp
v=1.0

secretclr() {
  for secret in $(docker secret ls -q); do
    docker secret rm "${secret}" >/dev/null 2>&1 \
      && echo "${secret} removed"
  done;
}

dkclr() {
  docker stop $(docker ps -a -q) \
    && docker rm $(docker ps -a -q) \
    && echo "Containers stopped and removed succesfully" \
    && docker ps -a
}

imageclean() {
  docker image rmi $ns/config-server:$v \
    && docker image rmi $ns/customer-service:$v \
    && docker image rmi $ns/hotel-service:$v
}

# Performs state cleaning
dkclr && secretclr && sleep 2 && imageclean && echo "Docker state cleaned" \
  && docker volume prune -f && docker network prune -f \
  && docker stack ls && docker ps -a && echo && echo \
  && docker service ls && echo && docker volume ls && echo \
  && docker network ls && echo && docker node ls

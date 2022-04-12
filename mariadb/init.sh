#!/bin/sh

prog_exists() {
  prog=$1
  command -v $prog >/dev/null 2>&1 || { echo >&2 "$prog is required for this script to run, but it's not installed. Aborting."; exit 1; }
}

test_host_connection() {
  remote_host=$1
  ssh -q $remote_host exit
  if [ $? -eq 0 ]; then
    echo "Host is available"
    return 0
  else
    echo "Host is not available"
    return 1
  fi
}

get_current_ctx() {
  docker context inspect --format '{{ .Name }}'
}

# parse flags
while getopts h:i:f: flag
do
 case "${flag}" in
   h) host=${OPTARG};;
   i) image=${OPTARG};;
   f) file=${OPTARG};;
 esac
done

original_host=$(get_current_ctx)

# check for mandatory flags
if [ -z $host ]; then
  echo "Host is not set, script cant be executed."
  exit 1
fi
if [ -z $image ]; then
  echo "Image is not set, script cant be executed."
  exit 1
fi
if [ -z $file ]; then
  echo "File is not set, script cant be executed."
  exit 1
fi

# check for prerequisite programs
prog_exists docker
prog_exists ssh

echo "Original host: $original_host"

if [ "$(get_current_ctx)" != "$host" ]; then
  echo "Host is different from current context"
  echo "Switching host to $host..."
  docker context use $host >/dev/null 2>&1
fi

test_host_connection $host
if [ $? -ne 0 ]; then
  echo "Host could not be reached"
  exit 1
fi

if [ ! -f "$file" ]; then
  echo "File from $file, could not be found. Aborting."
  exit 1
fi

container_health=$(docker container inspect $(docker ps -f ancestor=$image -q) --format "{{ .State.Health.Status }}")

until [ "$container_health" != "starting" ]; do
  container_health=$(docker container inspect $(docker ps -f ancestor=$image -q) --format "{{ .State.Health.Status }}")
  if [ "$cache" != "$container_health" ]; then
    echo "Waiting for container to startup, current container status is $container_health"
  fi
  cache=$container_health
  sleep 1
done

# Import schema
# sh init.sh -h x220 -i mariadb:10.3 -f ${PWD}/mariadb/init/init.sql
if [ "$(get_current_ctx)" == "$host" ]; then
  # perform healthcheck
  docker exec -t $(docker ps -f "ancestor=$image" -q) bash -c 'mysqladmin -p"$(< /run/secrets/MARIADB_PASSWORD)" ping -h localhost' >/dev/null 2>&1
  if [ $? -eq 0 ]; then
    # perform actions after succesful healthcheck
    echo "Copying init.cql from local directory int othe container root..."
    docker cp $file $(docker ps -f "ancestor=$image" -q):/ >/dev/null 2>&1 \
      && docker exec -t $(docker ps -f "ancestor=$image" -q) mysql --defaults-extra-file=my.cnf -e 'source init.sql; show databases' >/dev/null 2>&1 \
      && echo "init.sql import succesful"
  fi
else
  echo "Change context to proper host"
  exit 1
fi

if [ $? -eq 0 ]; then
  echo "Setting host back to the original..."
  docker context use $original_host
  exit 0
fi

#!/bin/sh
# libinit.sh 2022-04-02
# Provides functions for checking container health and initialization process.

set -e
set -u
set -o pipefail

# DEBUG
#set -x

prog_exists() {
  prog=$1
  command -v $prog >/dev/null 2>&1 || { echo >&2 "$prog is required for this script to run, but it's not installed. Aborting."; exit 1; }
}

localize_to_dir() {
  cd $(cd -P -- "$(dirname -- "$0")" && pwd -P)
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

# Outputs the current docker context to stdin
get_current_ctx() {
  docker context inspect --format '{{ .Name }}'
}

# Notice: docker context has to be local for this to work!
resolve_cid() {
  image=$1
  docker ps --filter "ancestor=$image" -q
}

# Notice: docker context has to be local for this to work!
resolve_cid_by_name() {
  name=$1
  docker ps --filter "name=$name" -q
}

# Stack name $1
# Name of the container $2
resolve_cid_stack_by_name() {
  docker stack ps $1 --filter "name=$2" -q
}

# Sleeps until container health is healthy
wait_until_healthy() {
  wait_time=0
  count=0
  container_id=$1
  wait_time=$2
  container_health=$(docker inspect $container_id --format "{{ .State.Health.Status }}")

  if [ ! $wait_time -eq 0 ]; then
    echo "Waiting for a moment for the state to converge..."
    sleep $wait_time
  fi

  container_health=$(docker inspect $container_id --format "{{ .State.Health.Status }}")

  until [ "$container_health" != "starting" ]; do
    container_health=$(docker container inspect $container_id --format "{{ .State.Health.Status }}")
    if [ $count -eq 0 ]; then
      echo "Waiting for container to startup, current container status is $container_health"
    fi
    if [ "$cache" != "$container_health" ]; then
      echo "Waiting for container to startup, current container status is $container_health"
    fi
    cache=$container_health
    count=$(expr $count + 1)
    sleep 1
  done
}

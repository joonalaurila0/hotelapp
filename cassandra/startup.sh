#!/bin/sh
#
# For initializing cassandra 4.0.1
# Example 1: sh cassandra/startup.sh -h x220 -i cassandra:4.0.1 -f ${PWD}/cassandra/schema-init/init.cql -c
# Example 2: sh cassandra/startup.sh -h x220 --stack cluster-test --swarm \
#                 --name cas-master --schema ${PWD}/cassandra/schema-init/schema.cql
#                 --data ${PWD}/cassandra/schema-init/data.cql --database hotelapp
#
# Cluster service naming convention: <cluster name>_<service_name>
# Example: Cluster: test-cluster, service: cas-master. Service name in stack: test-cluster_cas-master


set -e
set -u
set -o pipefail

# DEBUG
# set -x

command -v find >/dev/null 2>&1 || { echo >&2 "find is required for this script to run, but it's not installed. Aborting."; exit 1; }

# Defines project root
project_root="/home/$USER/Desktop/projects/java/hotelapp-final"

# locates libinit.sh
libinit_location=$(find $project_root -type f -iname '*.sh' -regex '.*libinit.*')

# Bring in common initialization utilities
. $libinit_location

# Move shell execution environment to the directory where shell script is being executed.
localize_to_dir

debug_img() {
  echo "------------------DEBUG------------------"
  echo "CURRENT CONTEXT: "
  docker ps -f "ancestor=$image"
  echo "------------------DEBUG------------------"
}

help_text() {
 cat << EOF

    [ Apache Cassandra Autosetup ]

    Usage: Please select a mode for the startup initialization of the Hashicorp Vault.
    run: sh startup.sh --help for more help
    Usage: sh startup.sh [OPTION...]
    To run the program, you must define either local or swarm mode with the flags -c or -s.
    Defining the image is also mandatory.
    Running the program without arguments defaults to help text seen here.
    Options:
  --help                          Display this help and exit.
  -h, --host                      Define host to use.
  -c, --compose                   Run with docker-compose.
  -s, --swarm                     Run as part of a swarm.
  -i, --image                     Define image to use.
  -f, --file                      Define file to feed to cassandra.
  --database                      Define database for the data and/or schema file (used by import_file function).
  --schema                        Define schema.cql file to feed to the cassandra database.
  --data                          Define data.cql file to feed to the cassandra database (Insertation data).
  --stack                         Specifies the stack.
  --name                          Specifies the name of a container. Note: You should use context-local names, script changes contexts.
EOF
exit 0
}

# Checks for commandline arguments.
parse_args() {
  local argc=$#
  local argv=$@

	while [ -n "${1-}" ]
	do
		case "$1" in
			"--help") help_text && exit 0
        ;;
      "--host" | "-h")
        echo "Setting host to $2 ..." \
          && host="$2"
        ;;
			"--compose" | "-c")
				echo "Initializing Cassandra with docker-compose..." \
					&& compose_mode=1 # starts up the vault using docker-compose
				;;
			"--swarm" | "-s") 
				echo "Initializing Vault as part of a swarm service..." \
					&& swarm_mode=1
				;;
      "--image" | "-i") 
        image="$2"
				;;
			"--file" | "-f") 
        file="$2"
				;;
			"--stack")
        stack=$2
				;;
			"--name")
        [ -z "$stack" ] && echo "Error: --stack argument must be set for --name to work, aborting..." && exit 1;
        name=$2
				;;
			"--schema")
        schema_file=$2
				;;
			"--data")
        data_file=$2
				;;
			"--database")
        database=$2
				;;
      "") help_text && exit 0
        ;;
		esac
		shift
	done
}

parse_args $# $@

sleep 2 # Wait a moment for the state to converge.

if [ -z "${image-}" ] && [ -z "${name-}" ]; then
  echo "WARNING! Image or name must be defined for this program to be run, as there is no default image nor name set. Aborting..."
  exit 1
fi

# Checks that both modes are not defined.
if [ ! -z "${compose_mode-}" ] && [ ! -z "${swarm_mode-}" -eq 1 ]; then
  echo "WARNING! You cannot have both local and remote mode at the sametime! Aborting..."
  exit 1
fi

# Checks that either one is defined.
if [ -z "${compose_mode-}" ] && [ -z "${swarm_mode-}" ]; then
  echo "WARNING! You must define either local or swarm mode! Aborting..."
  exit 1
fi

# Startup local Cassandra instance through docker-compose.
[ ! -z "${compose_mode-}" ] && docker-compose -f cassandra.yml -d up

# Show help text if no arguments are given.
if [ $# -eq 0 ]; then
  help_text && exit 0
fi

# Queries for localhost as the "master node".
original_host="$(get_current_ctx)"

# Check for prerequisite programs.
prog_exists docker
prog_exists ssh

# Changes to the host that is set from the command line arguments.
if [ "$(get_current_ctx)" != "$host" ]; then
  echo "Host is different from current context"
  echo "Switching host to $host..."
  docker context use $host >/dev/null 2>&1
  echo "Waiting a moment for the state to converge..."
  sleep 3
fi

# Test that host was succesfully changed and that connection can be made.
if [ "$host" != "default" ]; then
  test_host_connection $host
fi

sleep 2

cid=$(resolve_cid_by_name $name)
container_health=$(docker inspect $cid --format "{{ .State.Health.Status }}")
cache=$container_health
count=0

# args: container_id, wait_time
wait_until_healthy $cid 2 

# Switch back to master node.
#[ "$(get_current_ctx)" != $host ] && docker context use $original_host

import_file() {
  ctx=$host
  og_ctx=$original_host
  schema_file_with_location=$schema_file
  data_file_with_location=$data_file
  schema_file_bare=$(basename $schema_file)
  data_file_bare=$(basename $data_file)
  cid=$cid
  db=$database
  [ $(get_current_ctx) != "$host" ] && echo "Host is incorrect, aborting.." && exit 1;
  if [ "$(docker inspect $cid --format "{{ .State.Health.Status }}")" = "healthy" ]; then
    #cid=$(resolve_cid $name)
    docker cp $schema_file_with_location $cid:/ >/dev/null 2>&1 \
      && docker cp $data_file_with_location $cid:/ >/dev/null 2>&1 \
      && docker exec -t $cid cqlsh -f "$schema_file_bare" >/dev/null 2>&1 \
      && docker exec -t $cid cqlsh -f "$data_file_bare" >/dev/null 2>&1 \
      && docker exec -t $cid cqlsh -e "describe keyspaces; use $db; describe tables;"
  fi
}

[ ! -z "${database-}" ] && import_file

# Switch back to master node.
if [ "$(get_current_ctx)" != "default" ]; then
  echo "Setting host back to the original..."
  docker context use $original_host
  exit 0
fi

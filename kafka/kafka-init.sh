#!/bin/sh
# kafka-init.sh 2022-06-21
# For initializing kafka 
#
# sh kafka-init.sh --docker-ctx default --swarm \
#   --image quay.io/strimzi/kafka:0.29.0-kafka-3.2.0-amd64 \
#   --stack hotelapp --name kafka --root $PWD

set -e
set -u
set -o pipefail

# DEBUG
#set -x

help_text() {
 cat << EOF

    [ Apache Kafka Initializer ]

    Usage: Please select a mode for the startup initialization of the Apache Kafka for the Hotelapp application.
    run: sh startup.sh --help for more help
    Usage: sh startup.sh [OPTION...]
    To run the program, you must define either local or swarm mode with the flags -c or -s.
    Defining the image is also mandatory. Running the program without arguments defaults to help text seen here.
    Options:
  --help                          Display this help and exit.
  -r, --root                      Define the project root (Remember to set this!).
  --docker-ctx                    Defines docker context to switch to.
  -s, --swarm                     Run as part of a swarm.
  --swarmhost                     Defines the master/main node ip.
  -i, --image                     Define image to use.
  --stack                         Specifies the stack name, this parameter is necessary for swarm deployments.
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
      "--root" | "-r") 
        echo "Setting the project root to $2" && project_root="$2" # Defines the root for the project (used for moving files)
				;;
      "--docker-ctx")
        echo "Setting docker context to $2 ..." \
          && docker_host="$2"
        ;;
			"--compose" | "-c")
				echo "Initializing Cassandra with docker-compose..." \
					&& compose_mode=1 # starts up the vault using docker-compose
				;;
			"--swarm" | "-s") 
				echo "Initializing Vault as part of a swarm service..." \
					&& swarm_mode=1
				;;
			"--swarmhost") 
				echo "Setting swarmhost to $2" \
					&& SWARMHOST=$2
				;;
      "--image" | "-i") 
        image="$2"
				;;
			"--stack")
        stack=$2
				;;
			"--name")
        [ -z "$stack" ] && echo "Error: --stack argument must be set for --name to work, aborting..." && exit 1;
        name=$2
        ;;
      "") help_text && exit 0
        ;;
		esac
		shift
	done
}

parse_args $# $@

# Waiting a bit for the state to converge
sleep 10

# Show help text if no arguments are given.
if [ $# -eq 0 ]; then
  help_text && exit 0
fi

if [ -z $project_root ]; then
  echo "You need to define the project_root to start the initialization!"
  exit 1
fi



# #################################################
# Locates libinit in strangely obtuse manner,     #
# sources libinit once it is found and runs       #
# the "localize_to_dir" function from libinit     #
# to move the shell execution environment to      #
# the directory where shell script is being       #
# executed. This is done to move files around     #
#                                                 #
# NOTE: It is important that we first initialize  #
#       this variable and source libinit.sh,      #
#       this is needed for localize_to_dir.       #
###################################################

# locates libinit.sh
libinit_location=$(find $project_root -type f -iname '*.sh' -regex '.*libinit.*')

# Bring in common initialization utilities
. $libinit_location

# Move shell execution environment to the directory where shell script is being executed.
localize_to_dir


# Waiting some more for assurance
#
# NOTE: This is machine-dependent I'd assume, 
# since it would depend on how fast your 
# deployment will initialize, but if it has not,
# this will result in the container id not being found
# which will result in the whole script not working.
sleep 5


cid=$(resolve_cid_by_name $name)

if [ -z $cid ]; then
  echo "CID could not be found!"
  exit 1
fi

# Changes to the necessary docker context that is set from the command line arguments.
if [ "$(get_current_ctx)" != "$docker_host" ]; then
  echo "Host is different from current context"
  echo "Switching host to $docker_host..."
  docker context use $docker_host >/dev/null 2>&1
  echo "Waiting a moment for the state to converge..."
  echo -n -e "Current host changed to $(get_current_ctx)\n"
  sleep 3
fi



cid=$(resolve_cid_by_name $name)
container_health=$(docker inspect $cid --format "{{ .State.Health.Status }}")
cache=$container_health
count=0

wait_until_healthy $cid 2

docker exec -t $cid ./bin/kafka-topics.sh \
  --bootstrap-server=$(SWARMHOST):9092 \
  --create --topic hotel-service --replication-factor 1 --partitions 2

docker exec -t $cid ./bin/kafka-topics.sh \
  --bootstrap-server=$(SWARMHOST):9092 \
  --create --topic customer-service --replication-factor 1 --partitions 2

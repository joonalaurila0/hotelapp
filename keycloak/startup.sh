#!/bin/sh
# startup.sh 2022-03-28
# For deploying Redhat Keycloak 17.0.0
# Example: sh keycloak/startup.sh --host bob@192.168.239.133 --swarm --root $PWD

set -e
set -u
set -o pipefail

# DEBUG
#set -x

command -v find >/dev/null 2>&1 || { echo >&2 "find is required for this script to run, but it's not installed. Aborting."; exit 1; }

# Executables path.
kbin="/opt/keycloak/bin"

# setup.sh
ksetup="/opt/keycloak/bin/setup.sh"

# kcadm executable path
kcadm="/opt/keycloak/bin/kcadm.sh"

# Part of a swarm cluster?
# Defaults to 0 = false
swarm_mode=0

help_text() {
 cat << EOF

    [ Keycloak Autosetup ]

    Usage: Please select a mode for the startup initialization for Keycloak.
    run: sh startup.sh --help for more help
    Usage: sh startup.sh [OPTION...]
    Running the program without arguments defaults to help text seen here.
    Options:
  --help                          Display this help and exit.
  -h, --host                      Define the host for the instance.
  -r, --root                      defines the project root directory
  -i, --image                     Define the image for the instance.
  -c, --compose                   Run with docker-compose.
  -s, --swarm                     Run as part of a swarm.
  --stack                         parameter for swarm deployments to name the stack.
EOF
exit 0
}

# Defines default image
image="quay.io/keycloak/keycloak:17.0.0"

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
      "--stack") echo "Setting the stack to $2" && stack="$2" # Defines the stack name
        ;;
      "--host" | "-h") host=$2
        ;;
      "--image" | "-i") image=$2
        ;;
			"--compose" | "-c")
				echo "Initializing Keycloak with docker-compose..." # To notify the user about the mode.
				;;
			"--swarm" | "-s") 
				echo "Initializing Keycloak as part of a swarm service..." \
					&& swarm_mode=1
				;;
      "") help_text && exit 0
        ;;
		esac
		shift
	done
}

if [ $# -eq 0 ]; then
  help_text
fi

parse_args $# $@

sleep 2 # Wait for a bit for the state to converge

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

# Bring in common initialization utilities.
. $libinit_location

# Move shell execution environment to the directory where shell script is being executed.
localize_to_dir



# Check for prerequisite programs.
prog_exists docker
prog_exists ssh



#############################################################
# Start up the initialization                               #
# 0) run docker-compose deployment                          #
# 1) run swarm deployment (vault is ran as a service)       #
#                                                           #
# NOTE: Program assumes you to be operating at vault folder #
#############################################################
case "$swarm_mode" in
  1) docker stack deploy --compose-file keycloak.yml $stack \
    && sleep 3 # Wait a bit for the state to converge
    ;;
  0) docker-compose -f keycloak.yml up -d # Starts up the keycloak using docker-compose (default)
    ;;
esac
sleep 2



# Queries for localhost as the "master node".
original_host=$(get_current_ctx)

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

# Container ID
cid=$(resolve_cid $image)
# Container Health.
container_health=$(docker inspect $cid --format "{{ .State.Health.Status }}")
# Cached Container Health.
cache=$container_health
# For counting.
count=0

wait_until_healthy $cid 2

# Runs setup scripts from the container.
if [ "$container_health" = "healthy" ]; then
  cid=$(docker ps --filter "ancestor=$image" -q)
  docker cp setup.sh $cid:/opt/keycloak/bin/
  docker exec -t $cid sh /opt/keycloak/bin/setup.sh
fi

# Switch back to master node.
if [ "$(get_current_ctx)" != "default" ]; then
  echo "Setting host back to the original..."
  docker context use $original_host
  exit 0
fi

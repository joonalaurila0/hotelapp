#!/bin/sh
#
# Example: sh keycloak/startup.sh --host x200 --swarm

set -e
set -u
set -o pipefail
#set -x

command -v find >/dev/null 2>&1 || { echo >&2 "find is required for this script to run, but it's not installed. Aborting."; exit 1; }

# Executables path.
kbin="/opt/keycloak/bin"

# setup.sh
ksetup="/opt/keycloak/bin/setup.sh"

# kcadm executable path
kcadm="/opt/keycloak/bin/kcadm.sh"

# Defines project root.
project_root="/home/$USER/Desktop/projects/java/hotelapp"

# locates libinit.sh
libinit_location=$(find $project_root -type f -iname '*.sh' -regex '.*libinit.*')

# Bring in common initialization utilities.
. $libinit_location

# Move shell execution environment to the directory where shell script is being executed.
localize_to_dir

# Part of a swarm cluster?
swarm_mode=0

# Check for prerequisite programs.
prog_exists docker
prog_exists ssh

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
  -i, --image                     Define the image for the instance.
  -c, --compose                   Run with docker-compose.
  -s, --swarm                     Run as part of a swarm.
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
      "--host" | "-h") host=$2
        ;;
      "--image" | "-i") image=$2
        ;;
			"--compose" | "-c")
				echo "Initializing Keycloak with docker-compose..." \
					&& docker-compose -f keycloak.yml up -d # starts up the vault using docker-compose
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

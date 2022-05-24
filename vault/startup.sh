#!/bin/sh

set -e
set -u
set -o pipefail
set -x

# Saves Vault's unseal tokens into the token_dir variable file.
# This file is used by the unseal.sh file to unseal the vault,
# by parsing through the unseal keys using jq.

command -v find >/dev/null 2>&1 || { echo >&2 "find is required for this script to run, but it's not installed. Aborting."; exit 1; }

# Defines project root
project_root="/home/$USER/Desktop/projects/java/hotelapp-final"

# locates libinit.sh
libinit_location=$(find $project_root -type f -iname '*.sh' -regex '.*libinit.*')

# Bring in common initialization utilities
. $libinit_location

# Move shell execution environment to the directory where shell script is being executed.
localize_to_dir

# Check for needed programs
prog_exists docker
prog_exists jq

# Preset variables
token_dir="vault/data/tokens.json"
swarm_mode=0
autoinit_mode=0
stack="hotelapp"

help_text() {
 cat << EOF

    [ HashiCorp Vault Autosetup ]

    Usage: Please select a mode for the startup initialization of the Hashicorp Vault.
    run: sh startup.sh --help for more help
    Usage: sh startup.sh [OPTION...]
    Running the program without arguments defaults to help text seen here.
    Options:
  -h, --help                 display this help and exit
  -c, --service                   run with docker-compose
  -s, --swarm                     run as part of a swarm
  --stack                         optional parameter to name the stack
  -i, --auto-init                 automatically initialize
EOF
exit 0
}

# pass cid as first arg, import path second
vault_init() {
  local cid=$1
  local import_path=$2
  docker exec -t $cid vault operator init | tee $import_path \
    && echo "Saved tokens at $import_path"
}

query() {
  prompt=$1
  cid=$cid
  echo -n "$1"
  while :
  do
    read input_string
    case $input_string in
      Y|y|Yes|yes|"")
        vault_init $cid $token_dir
        break
        ;;
      N|n|No|no)
				echo "Process postponed, cleaning docker state... "
				docker stop $cid \
							&& docker rm $cid \
							&& echo "Containers succesfully stopped and cleaned up" && docker ps -a
				exit
        ;;
      *)
        echo "Please pick yes or no!"
        ;;
    esac
  done
}

# Checks for commandline arguments.
parse_args() {
  local argc=$#
  local argv=$@

	while [ ! $# -eq 0 ]
	do
		case "$1" in
			--help | -h) help_text && exit 0
        ;;
      --stack) stack=$1
        ;;
			--compose | -c)
				echo "Initializing Vault with docker-compose..." \
					&& docker-compose -d -f vault-deploy.yml up # starts up the vault using docker-compose
				;;
			--swarm | -s) 
				echo "Initializing Vault as part of a swarm service..." \
					&& swarm_mode=1 \
          && docker stack deploy --compose-file vault-deploy.yml $stack \
          && sleep 3 # Wait a bit for the state to converge
				;;
      --auto-init | -i) 
        autoinit_mode=1 && break # skips query and startups vault operator init
				;;
                    "") help_text && exit 0
        ;;
		esac
		shift
	done
}

# If no commandline arguments were given, give help text.
if [ $# -eq 0 ]; then
  help_text
fi

parse_args $# $@

sleep 2 # Wait a moment for state convergence.

image="vault:1.9.2"
cid=$(resolve_cid $image)

if [ -z $cid ]; then
  echo "Something went wrong, couldn't find the Vault container!"
  exit 1
fi

case "$autoinit_mode" in
  1) vault_init $cid "vault/data/tokens.json"
    ;;
  0) query "Are you ready to initalize HashiCorp Vault? [Y]es/[N]o? "
    ;;
esac

if test -f "$token_dir"; then
  echo "Starting unsealing process..."
  . ./vault/unseal.sh
else
  echo "Missing tokens.json from $token_dir.json"
fi

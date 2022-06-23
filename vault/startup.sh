#!/bin/sh
# startup.sh 2022-03-13
# For deploying HCP Vault.

set -e
set -u
set -o pipefail

# DEBUG
#set -x

# Saves Vault's unseal tokens into the token_dir variable file.
# This file is used by the unseal.sh file to unseal the vault,
# by parsing through the unseal keys using jq.

command -v find >/dev/null 2>&1 || { echo >&2 "find is required for this script to run, but it's not installed. Aborting."; exit 1; }

# Preset variables
token_dir="vault/data/tokens.json"
swarm_mode=0
autoinit_mode=0

help_text() {
 cat << EOF

    [ HashiCorp Vault Autosetup ]

    Usage: Please select a mode for the startup initialization of the Hashicorp Vault.
    run: sh startup.sh --help for more help
    Usage: sh startup.sh [OPTION...]
    Running the program without arguments defaults to help text seen here.
    Options:
  -h, --help                 display this help and exit
  -c, --service                   run with docker-compose (default)
  -r, --root                      defines the project root directory
  -s, --swarm                     run as part of a swarm (Remember to define stack as well with --stack)
  --stack                         parameter for swarm deployments to name the stack
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

	while [ -n "${1-}" ]
	do
		case "$1" in
			--help | -h) help_text && exit 0
        ;;
      --root | -r) 
        echo "Setting the project root to $2" && project_root="$2" # Defines the root for the project (used for moving files)
				;;
      --stack) echo "Setting the stack to $2" && stack="$2" # Defines the stack name
        ;;
			--compose | -c)
				echo "Initializing Vault with docker-compose..." # This is only to signify to the user that swarm mode is off
				;;
			--swarm | -s) 
          [ -z "$stack" ] && echo "Error: --stack argument must be set for --swarm to work, aborting..." && exit 1;
          echo "Initializing Vault as part of a swarm service..." && swarm_mode=1
				;;
      --auto-init | -i) 
        autoinit_mode=1 # skips query and startups vault operator init
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



# ###############################
# Checks for necessary programs #
#################################
prog_exists docker
prog_exists jq
prog_exists yq



#############################################################
# Start up the initialization                               #
# 0) run docker-compose deployment                          #
# 1) run swarm deployment (vault is ran as a service)       #
#                                                           #
# NOTE: Program assumes you to be operating at vault folder #
#############################################################
case "$swarm_mode" in
  1) docker stack deploy --compose-file vault-deploy.yml $stack \
    && sleep 3 # Wait a bit for the state to converge
    ;;
  0) docker-compose -f vault-deploy.yml up -d # Starts up the vault using docker-compose (default)
    ;;
esac


# Image name and tag is defined here.
image="vault:1.9.2"
# Image container is captured to a variable.
cid=$(resolve_cid $image)


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

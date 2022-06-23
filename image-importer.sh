#!/bin/sh
# image-importer.sh 2022-06-12
# Build the project before running this.
# Sends a container image through rsync for another host.
# Testing to do actions on another host with ssh and docker.

set -e
set -u
set -o pipefail
set -x

command -v rsync >/dev/null 2>&1 || { echo >&2 "rsync is required for this script to run, but it's not installed. Aborting."; exit 1; }
command -v ssh >/dev/null 2>&1 || { echo >&2 "ssh is required for this script to run, but it's not installed. Aborting."; exit 1; }

help_text() {
 cat << EOF
    [ Import a docker/OCI image to another host ]

    Build the project before running this !

    run: sh import-image.sh --help for more help
    Usage: sh import-image.sh [OPTION...]
    To run the program, you must define the target host -h, the image -i, the tar file name and the location -l.
    Running the program without arguments defaults to help text seen here.
    Options:
  --help                          Display this help and exit.
  -h, --host                      Define remote host which to import the image into.
  -l, --location                  The location to which import the image into (the tar file).
  -t, --tar                       Tar archive name for the image to be imported, remember to specify extension e.g. ".tar", ".tar.gz", etc.
  -i, --image                     Specify the image name and tag which to import, FORMAT: REPOSITORY:TAG. Example: hotelapp/config-server:1.0
EOF
exit 0
}

# Logs the actions
LOG_FILE="ssh_actions.log"

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
        echo "Setting remote host to $2 ..." \
          && host=$2
        ;;
			"--image" | "-i")
				echo "Setting the image to $2" \
					&& image=$2 # starts up the vault using docker-compose
				;;
			"--location" | "-l") 
				echo "Setting the location to $2" \
					&& location=$2
        ;;
			"--tar" | "-t") 
				echo "Setting the tar archive to $2" \
					&& tar=$2
        ;;
      "") help_text && exit 0
        ;;
		esac
		shift
	done
}

# Take in the arguments
parse_args $# $@

# Export the image into a tar file.
make save

# Copy the image over to another host.
rsync -avz -P -e 'ssh' $tar $host:$location

# Go to the host, remove the old archive of the image from the repository, 
# then load the new image from current directory of the new archive with docker load.
ssh -t $host "cd $location && docker rmi $image && docker load < $tar"

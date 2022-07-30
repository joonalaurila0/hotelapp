#!/bin/sh
# startup.sh 2022-07-30
# Starts up all the necessary application's to run the project.

set -e
set -u
set -o pipefail

# DEBUG
#set -x

query() {
  prompt=$1
  echo -n "$1"
  while :
  do
    read input_string
    case $input_string in
      Y|y|Yes|yes|"")
        break;
        ;;
      N|n|No|no)
				exit 0
        ;;
      *)
        echo "Please pick yes or no!"
        ;;
    esac
  done
}

help_text() {
 cat << EOF

    [ How do I run this? ]

    Usage: To run simply execute the script, you can also provide additional options if you like.
    ----------------------------------------------
    NOTE: This is to be run from the project root!
    ----------------------------------------------
    --help for more help
    ----------------------------------------------
    Usage: sh startup.sh [ADDITIONAL OPTION(S)...]
    ----------------------------------------------
    Options:
    --help                          Shows this help text.
    -skip, --skip-query             Omits the query about sourcing environmental variables before running the script.
    ----------------------------------------------
EOF
exit 0
}

skip=0

parse_args() {
  local argc=$#
  local argv=$@
	while [ -n "${1-}" ]
	do
		case "$1" in
			"--help") help_text && exit 0
        ;;
      "--skip-query" | "-skip") 
        skip=1
				;;
		esac
		shift
	done
}

parse_args $# $@

[ "${skip-}" -ne 1 ] && query '
    Have you recalled to source the environmental variables before launching this script? (source .envs) [Y]es/[N]o?
    -------------------------------
    Do not forget to source .envs !
    -------------------------------
    [Y]es/[N]o? '


source ./.envs # sources environmental variables, but this only works for the shell script session.

make initialize && \
  make external && \
  make cfg discovery && \
  make services && \
  make gateway && \
  make client

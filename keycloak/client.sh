#!/bin/sh

set -e
set -u
set -o pipefail

command -v curl >/dev/null 2>&1 || { echo >&2 "curl is required for this script to run, but it's not installed. Aborting."; exit 1; }

token_url="http://localhost:8080/realms/master/protocol/openid-connect/token"
auth_url="http://localhost:8080/realms/master/protocol/openid-connect/auth"
introspect_uri="http://localhost:8080/realms/master/protocol/openid-connect/token/introspect"
secret="SU6yBAxXAZVidMDnZ2vrFmENEHTCX2lW"

# Example:
#
# sh client.sh -t alkjfdalfdjk -cc
#

call_creds() {
  curl http://localhost:8080/realms/master/protocol/openid-connect/token \
    -d "client_id=gateway-api" \
    -d "client_secret=$secret" \
    -d "grant_type=client_credentials"
}

call_kc() {
  curl -v $introspect_uri \
    -d "client_id=gateway-api" \
    -d "client_secret=$secret" \
    -d "token=$token"
}



parse_args() {
  local argc=$#
  local argv=$@

	while [ -n "${1-}" ]
	do
		case "$1" in
			"--help") help_text && exit 0
        ;;
      "--token" | "-t") token=$1
        ;;
      "--client-credentials" | "-cc") call_kc
        ;;
      "--client-credentials-token" | "-cct") call_creds
        ;;
      "") help_text && exit 0
        ;;
		esac
		shift
	done
}

parse_args $# $@

# Show help text if no arguments are given.
if [ $# -eq 0 ]; then
  help_text && exit 0
fi

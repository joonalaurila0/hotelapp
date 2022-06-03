#!/bin/sh
# sh auth.sh -u testuser@meow.com -p meow -s zjpSKq1HEj2RRqiIpb -g password
# sh auth.sh -u testuser@meow.com -p meow -s zjpSKq1HEj2RRqiIpb -g authorization_code
# curl -v -X POST "http://localhost:8080/realms/master/protocol/openid-connect/auth" -d "username=testuser@meow.com" -d "password=meow" -d "grant_type=authorization_code" -d "client_id=hotelapp" -d "client_secret=zjpSKq1HEj2RRqiIpb" -d "response_type=code"
# localhost:8080/realms/master/protocol/openid-connect/auth?client_id=hotelapp&client_secret=zjpSKq1HEj2RRqiIpb&response_type=code

command -v curl >/dev/null 2>&1 || { echo >&2 "curl is required for this script to run, but it's not installed. Aborting."; exit 1; }

set -e
set -u
set -o pipefail

token_url="http://localhost:8080/realms/master/protocol/openid-connect/token"
auth_url="http://localhost:8080/realms/master/protocol/openid-connect/auth"
secret="SU6yBAxXAZVidMDnZ2vrFmENEHTCX2lW&"

# parse flags
while getopts u:p:s:g: flag
do
 case "${flag}" in
   u) username=${OPTARG};;
   p) password=${OPTARG};;
   s) secret=${OPTARG};;
   g) grant_type=${OPTARG}
 esac
done

grant_type_password() {
  curl -v -X POST $token_url -H "Content-Type: application/x-www-form-urlencoded" \
    -d "username=$username" \
    -d "password=$password" \
    -d "grant_type=password" \
    -d "client_id=hotelapp" \
    -d "client_secret=$secret"
}

auth_code_login() {
  curl -v -X GET \
    "localhost:8080/realms/master/protocol/openid-connect/auth?client_id=hotelapp&client_secret=zjpSKq1HEj2RRqiIpb&grant_type=authorization_code&response_type=code"
}

grant_type_auth_code() {
  curl -v -X POST $url -H "Content-Type: application/x-www-form-urlencoded" \
    -d "username=$username" \
    -d "password=$password" \
    -d "grant_type=authorization_code" \
    -d "client_id=hotelapp" \
    -d "client_secret=$secret"
}

grant_type_client_creds() {
  curl -v http://localhost:8080/realms/master/protocol/openid-connect/token/introspect \
    --data "client_id=gateway-api&client_secret=$secret&token=$token" 
}

case $grant_type in
  "password") grant_type_password
    ;;
  "authorization_code") auth_code_login
    ;;
  "client_creds") grant_type_client_creds
esac

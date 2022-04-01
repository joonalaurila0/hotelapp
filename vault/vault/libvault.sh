#!/bin/sh

# This file contains some functions for handling HashiCorp Vault
# Written 2022
# HashiCorp Vault 1.9.2

set -e
set -u
set -o pipefail
#set -x

show_curdir() {
  dir=$1
  echo "------------ DEBUG -----------------"
  echo "Current directory is: $dir"
  echo "------------ DEBUG -----------------"
}

# takes in unseal keys as the first argument, to unseal the vault
unseal_vault() {
  keys=$1
  cid=$2
  unseal_progress=0
  # unseal the vault for writing
  while [ "$unseal_progress" -lt 3 ]
  do
    current_key=$(echo ${keys[$unseal_progress]} | sed 's/"//g')
    echo "Starting unsealing..."
    docker exec -t $cid vault operator unseal $current_key \
      && unseal_progress=`expr $unseal_progress + 1` && echo "Unseal key $unseal_progress succesfully inserted"
    if [[ $status -ne 0 ]]; then
      echo "Something went wrong, could not reach the API."
      exit 1
    fi
  done
}

# logs in to the vault using root token from token_dir file
login_with_root_token() {
  log_save_location=$1
  token_dir=$2
  cid=$3
  status=$?
  # login
  echo "Logging in with root token..."
  docker exec -t $cid vault login $(jq '.root_token' $token_dir | sed 's/"//g') | tee $log_save_location \
    && echo "Login succesful, login credentials saved at $log_save_location"

  if [ $status -ne 0 ]; then
    echo "Something went wrong with generating login"
    exit 1
  fi

  if [ ! -f $log_save_location ]; then
    echo "Login file missing from $log_save_location, cannot proceed!"
    exit 1
  fi
}

copy_to_vault() {
  local path_from=$1
  local path_to=$2
  local cid=$3

  docker cp $path_from $cid:$path_to \
    && echo "$path_from copied to Vault at $2"
}

copy_from_vault() {
  local path_from=$1
  local path_to=$2
  local cid=$3

  docker cp $cid:$path_from $path_to \
    && echo "$path_from copied from Vault to $2"
}

configure_database_vault() {
  local plugin_name=$1
  local connection_ip=$2
  local allowed_roles=$3
  local username=$4
  local password=$5
  local database=$6
  local cid=$7

  docker exec -t $cid vault write database/config/$database \
      plugin_name=$plugin_name \
      connection_url="{{username}}:{{password}}@tcp($connection_ip:3306)/" \
      allowed_roles="$allowed_roles" \
      username="$username" \
      password="$password" \
      && echo "Database configuration successful for $plugin_name"
}

configure_dbrole_vault() {
  local database=$1
  local allowed_roles=$2
  local default_ttl=$3
  local max_ttl=$4
  local cid=$5

  docker exec -t $cid vault write database/roles/$allowed_roles \
    db_name=$database \
    creation_statements="CREATE USER '{{name}}'@'%' IDENTIFIED BY '{{password}}';GRANT SELECT ON *.* TO '{{name}}'@'%';" \
    default_ttl=$default_ttl \
    max_ttl=$max_ttl \
    && echo "Database role configuration configured for database/roles/$allowed_roles"
}

# import_location takes path as argument
import_tokens() {
  import_location=$1
  root_token=$2

  # Replaces the token on configuration server's yaml file.
  yq -i ".spring.cloud.config.server.vault.token = \"$root_token\"" "$import_location"/application.yml \
    && echo "Vault root token was assigned for the configuration server"

  # Finds clients of the configuration server, but excludes discovery server,
  # since it doesn't use vault. Replaces the vault tokens on the yaml files.
  find "$import_location"/config/ -type f -name "*.yml" -not \( -path "*/eureka.yml" -prune \) -exec yq -i ".spring.cloud.vault.token = \"$root_token\"" {} \; \
    && echo "Vault root token was assigned for the configuration server's client"

  # check that config-server has application.yml file
  if [ ! -f "$import_location/application.yml" ]; then
    echo "Token.yml could not be found from configuration server, process aborted."
    exit 1
  fi
}

# takes in location as the first agument and 
# data to put in the location as the second argument
import_data_to_vault() {
  key=$1
  location=$2
  data=$3
  cid=$4
  status=$?

  docker exec -t $cid vault kv put $key/$location $data \
    && docker exec -t $cid vault kv get $key/$location | jq

  if [ $status -ne 0 ]; then
    echo "Something went wrong with putting in or getting data to or from kv/"
    exit 1
  fi
}

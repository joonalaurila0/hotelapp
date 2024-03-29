#!/bin/sh
# Sourced by startup.sh
# This is to setup Hashicorp Vault

set -e
set -u
set -o pipefail

# DEBUG
#set -x

# load library for dealing with vault
. ./vault/libvault.sh

status=$?

if [ ! -f $token_dir ]; then
  echo "Tokens file missing from $token_dir, cannot proceed!"
  exit 1
fi

keys=($(jq '.unseal_keys_b64 | .[]' $token_dir))
root_token=$(jq '.root_token' $token_dir | sed 's/"//g')
unseal_progress=0

image="vault:1.9.2"
cid=$(resolve_cid $image)

# takes keys and container id
unseal_vault $keys $cid

vault_sealed=$(docker exec -t $cid vault status | jq '.sealed')

if [ $status -ne 0 ] && [ $vault_sealed != "false" ]; then
  echo "Something went wrong with unsealing"
  exit 1
fi

docker exec -t $cid vault status | jq && echo "Vault unsealed."

sleep 1

login_with_root_token "vault/data/login.json" $token_dir $cid

# Enable the database secrets engine, mount secrets engine at secret/ and 
# check secrets list, copies test-policy.hcl and data.json to container root at /
#dk exec -t vault192 vault secrets enable -path=secret -version=2 kv
docker exec -t $cid vault secrets enable database \
  && docker exec -t $cid vault secrets enable -path=kv -version=2 kv \
  && docker exec -t $cid vault secrets list \
  && docker cp vault/policies/test-policy.hcl $cid:/

#  && docker exec -t $cid vault secrets enable -path=secret/ kv \
if [ $status -ne 0 ]; then
  echo "Something went wrong with last copying over data to the container or mounting the secrets engine."
  exit 1
fi

# enable secrets engine kv version 2 at /secret
docker exec -t $cid vault secrets enable -path=secret -version=2 kv
docker exec -t $cid vault secrets list

copy_to_vault "vault/data/cass2.json" "/" $cid
copy_to_vault "vault/data/cassandra.json" "/" $cid
copy_to_vault "vault/policies/test-policy.hcl" "/" $cid

private_ip=$(/sbin/ip -o -4 addr list eno1 | awk '{print $4}' | cut -d/ -f1)

#configure_database_vault "mysql-database-plugin" "$private_ip" "my-role" "root" "32" "hotelapp" $cid
#configure_dbrole_vault "hotelapp" "my-role" "1h" "24h" $cid

## put in data.json file in kv/application
## secrets are created based on application naming scheme
import_data_to_vault "secret" "customer-service" "@cass2.json" $cid
import_data_to_vault "secret" "hotel-service" "@cassandra.json" $cid

# Overwrites the tokens for the yaml files.
import_tokens "../config-server/src/main/resources" $root_token

# Overwrites the current build's resources for the jar.
# This assures the application wont have to be rebuild each time tokens are changed.
# Unfortunately this only goes for the local deployment, since if the deployment
# is across multiple hosts, the application jar needs to be rebuilt for the
# docker images that get deployed to the other hosts.
import_tokens "../config-server/build/resources/main" $root_token

docker ps -a && echo "Succesful deployment of Vault :)"

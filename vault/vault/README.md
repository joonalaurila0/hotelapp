# About

Config directory contains configuration for Hashicorp Vault

It is very important that `VAULT_ADDR` and `VAULT_TOKEN` environment variables are mapped to their respective values like "http://127.0.0.1:8200" and "EoEj8r5XsZZNwT7CTB77", otherwise you wont be able to use the vault's cli properly and will most likely have errors.

Once you have set the token for vault you can copy the data.json file with `docker cp data.json <container id>:/` and then start a session in the container with `docker exec` and use `vault kv put secret/application @data.json` to push the data to the secret/application path.

the *.json files are the secrets for Vault, do not commit these files to git.

## Naming paths

You should name the paths by the application names of the services because thats how they're fetched by default by the Vault Config, e.g. `vault kv put secret/customer mariadb.username=root mariadb.password=supersecret` or `vault kv put secret/hotel cassandra.username=cassandra cassandra.password=cassandra` and to get `vault kv get secret/<application-name>`.

### TODO

Automate all the security and token features to come up automatically based on files

1. docker-compose up -d
2. docker exec -t $(docker ps -f "name=hotelapp-vault-1" -q) vault operator init | tee vault/tokens.txt
3. sh vault/unseal.sh

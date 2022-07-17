# What is this?

* This is premade, minimal initialization and setup for HashiCorp Vault version 1.9.2

To set it up run: `sh startup.sh`

Requirements are docker, docker-compose, jq, and bash shell. Make sure you also have the data from /vault directory.

# Running HashiCorp Vault

Vault can be deployed and initialized with: `$ make hcpvault`.  

Vault can also be optionally deployed using docker-compose with `$ sh vault/startup.sh -c -i --root ${PWD}`.  

The sensitive data is in the `vault/vault/data` directory, where you can store data to. `vault/vault` directory also contains configuration for the vault and policies. Because this is ridicously sluggish deployment already, I keep .envs file in this directory to define the environmental variables used in the make recipes, so I can set them fast. The .envs file is just a simple bash script that sets the environmental variables.
  
NOTE: `$ make hcpvault` uses predefined arguments to run the vault/startup.sh script, you can change these arguments in the Makefile yourself if you wish or just run the script yourself with arguments `$ sh vault/startup.sh --help` for more.

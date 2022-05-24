# Simple startup guide

**Note: For all setups (including local!) you should have ensure you have network and volume setup.**

Ensure you have these: `make network volume`, this creates the docker volume and network necesary for the container.

For local setup, use: `make local`.

For swarm setup, use: `make master`.

Setup second node: `make pair`.

# Bootstrap script

Get more information by running `sh startup.sh --help`

Example: `sh startup.sh -h x220 --swarm --stack cluster-test --name cas-master --schema ${PWD}/schema-init/schema.cql --data ${PWD}/schema-init/data.cql --database hotelapp`
Example: `sh startup.sh -h x220 --swarm --stack cluster-test --name cas-slave --schema ${PWD}/schema-init/schema2.cql --data ${PWD}/schema-init/data2.cql --database hotelapp2`
Example: `sh startup.sh -h x220 -i cassandra:4.0.1 -f ${PWD}/cassandra/schema-init/init.cql --swarm`

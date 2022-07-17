# Running Apache Cassandra

You can deploy cassandra instance using `$ make casmaster`.
  
This deploys and initializes Apache Cassandra instance with predefined scheme and data for the hotelapp application. Note that the cassandra/startup.sh is merely for initialization and the actual deployment is done by a make recipe -> `$ make master` or `$ make pair`. cassandra.yml contains the compose file deployment configuration for the master node and second\_node.yml contains the configuration for the second node in the cluster.

Initialization for the schemas and data for the databases are in the schema-init directory.
  
You can change these arguments to your liking by changing the Makefile or just using the script yourself: `$ sh casssandra/startup.sh --help`.  

# Simple startup

**Note: For all setups (including local!) you should have ensure you have network and volume setup.**

Ensure you have these: `$ make network volume`, this creates the docker volume and network necesary for the container.

For local setup that only deploys single master node, use: `$ make local`.

For swarm setup that only deploys single master node, use: `$ make master`.

Setup second node: `$ make pair`.

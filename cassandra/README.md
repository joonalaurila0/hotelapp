# Running Apache Cassandra

You can deploy cassandra instance using `$ make casmaster`.
  
This deploys and initializes Apache Cassandra instance with predefined scheme and data for the hotelapp application. Note that the cassandra/startup.sh is merely for initialization and the actual deployment is done by a make recipe -> `$ make master` or `$ make pair`
  
You can change these arguments to your liking by changing the Makefile or just using the script yourself: `$ sh casssandra/startup.sh --help`.  

# Structure of the application

Gateway guards the microservices and uses Keycloak as the STS into the application.  

If you try to deploy this app with: `$ docker stack deploy --compose-file docker-compose.yml multi-tier-app`, you will most likely be greeted by "secret not found error". Swarm requires that all cluster-level resources that a service depends on exist prior to proceeding with the deployment. So Docker halted the application deployment when it determined a resource dependency was missing.

The missing cluster-level resource that this application depends on is the POSTGRES\_PASSWORD secret. Recall that the application’s reference to that secret said that it was defined externally, if you look at the compose file `secrets` property. In this context, external means defined outside the application deployment definition and provided by Swarm.


# Simple startup

**Note: For all setups (including local!) you should have ensure you have network and volume setup.**

Ensure you have these: `$ make network volume`, this creates the docker volume and network necesary for the container.

For local setup, use: `$ make local`.

For swarm setup, use: `$ make master`.

Setup second node: `$ make pair`.

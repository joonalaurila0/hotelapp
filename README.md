# Hotel booking demo application

![Preview image of the client](./client/public/static/hotelapp_preview.webp)

Demo of a hotel booking application, books hotel rooms for the clients of the hotels. It is meant to act as a simple microservices deployment project.  

Configuration server segregates and centralizes the application configuration data from the services and maintains the application properties for them, acting as a centralized configuration. Configuration server uses HashiCorp Vault configuration repository to store the application properties that are sensitive. HashiCorp Vault is itself the backend for the configuration server to securely access the properties.  

Service Registry is implemented by Spring Cloud Eureka built in the discovery directory. Services register themselves to the registry, other services can obtain the location of a service instance by querying the service registry, which knows the locations of all service instances. This is further utilized by the API Gateway, which does the service routing, to provide a single entry point for all the services. It implements API Gateway pattern, all the traffic routes through API Gateway and the API Gateway will route the requests to the appropriate services.  

Redhat Keycloak acts as an STS/Authorization server for the API Gateway and the client, handling user authentication and authorization. Client for the project is deployed on nginx 1.22.0 utilizing HTTP/2 with TLS, gzip compression, cors, xsrf protection and caching.  

Persistence of the services is handled through Apache Cassandra, which deploys two nodes by default. Comes with built-in cluster, eventual consistency and linear write scalability, making it decent fit for the service oriented structure of project, although the deployment model itself is contrived for the sake of learning.  

The services themselves are implemented in the customer-service and hotel-service directories, both which interface with Apache Cassandra instances. Hotel-service is the handles giving out the hotels, cities, bookings and rooms for the client and customer-service handles user data like keeping data about the users themselves and invoices.  

Redis acts as a distributed cache for the services and Apache Kafka is used to implements logging through out services.  

Elasticstack (Elasticsearch, Logstash and Kibana) is utilized for transforming, storing, visualizing and querying all the logging data from the services and implementing a cetrnalized logging.

# How do I run this application?

**NOTE: Remember to source .envs!**

0. `$ source .envs`
1. `$ make initialize`, initializes necessary volumes, networks and secrets.
2. `$ make external`, deploys Apache Cassandra instances, HCP Vault, Redhat Keycloak, Apache Kafka, Redis and the Elasticstack (all the "external" applications).
3. `$ make cfg discovery`, builds and deploys the configuration server and discovery.
4. `$ make services`, builds and deploys all the services being used by the client.
5. `$ make gateway`, builds and deploys the Gateway, reserved as the second last operation so it access all the services immediately.
6. `$ make client`, builds and deploys the client, reserved as the last operation so it access the gateway, although this is not strictly necessary until request to services are made.

**NOTE: Current setup uses environment variables, make sure you have defined these beforehand!**

I am using .envs file where I set SWARMHOST, DOCKER\_CTX\_<number> for the deployment, only thing I need to do is to source the file:  
```bash
$ source .envs
```
    
These environment variables are mainly used for cassandra, importing docker images onto other hosts and in the client deployment.

**NOTE on building config-server, gateway and discovery: Current setup has config-server, discovery and gateway using image-importer.sh script to update images on the other host. To put this off the current build cycle, you only need to take off the $(MAKE) import invocation on the recipes in question.**

Deployment has only been tested on x86_64 GNU/Linux, 5.10.0-13-amd64 and subsequent remote machines are assumed to have x86_64 GNU/Linux with **required software**.

# Multihost or single host deployment?

If you're using a multihost deployment (this project is configured by default to use three host deployment [not that I would advocate for anyone to trying deploy this.]), you can define environment variables in the .envs file.

For example: `$ export REMOTEHOST_ONE=myuser@192.168.239.133`.

It is also possible to run this application on a single host, if you have computer that has required memory and processing power. Currently however this deployment is tuned for multihost setup.

_NOTE: I have not tried single host deployment with all the applications running, only multihost, so I cannot say how runnable it actually is, but with little tweaks it should not be hard to come by._

**Note! Directory names are used by the deployment and build scripts to a significant enough extent, so that changing names of them may break functionality of some of the scripts.**

# Required software

Docker +20.10.5  
GCC +10.2  
GNU bash, version 5.1.4(1)-release   
OpenSSH\_8.4p1  
find (GNU findutils) 4.8.0  
GNU Make 4.3  
sed (GNU sed) 4.7  
jq-1.6  
yq 4.25.1  
openjdk 17.0.3 2022-04-19  
OpenJDK Runtime Environment (build 17.0.3+7)   
OpenJDK 64-Bit Server VM (build 17.0.3+7, mixed mode)  
Gradle +7.4.2  
Node +v12.22.12   
npm +7.5.2   

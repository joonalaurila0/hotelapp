# How do I deploy this application?

1. `$ make initialize` or `$ make network && make secret && make casinit`
2. `$ make casmaster`
3. `$ make caspair`
4. `$ make hcpvault`


Gateway guards the microservices and uses Keycloak as the STS into the application.

If you try to deploy this app with: `docker stack deploy --compose-file docker-compose.yml multi-tier-app`, you will most likely be greeted by "secret not found error". Swarm requires that all cluster-level resources that a service depends on exist prior to proceeding with the deployment. So Docker halted the application deployment when it determined a resource dependency was missing.

The missing cluster-level resource that this application depends on is the POSTGRES\_PASSWORD secret. Recall that the application’s reference to that secret said that it was defined externally, if you look at the compose file `secrets` property. In this context, external means defined outside the application deployment definition and provided by Swarm.

To view secrets you can use `docker secret ls`

**Note! Directory names are used by the deployment and build scripts to a significant enough extent, so that changing names of them may break functionality of some of the scripts.**

# Ports

* Cassandra 9042, 7000
* Vault 8200

* Configuration-server 8888
* Service-Discovery 8761
* API-Gateway 8072
* Authorization-server 8443

* Customer-service 8000
* Hotel-service 8001

* Client 3000

## How to store the password in the file for deployment

You can run: `echo 'yeeting123' | docker secret create <application>-POSTGRES_PASSWORD -`

The only place this password is defined is in this Docker secret managed by Swarm. You can use any valid PostgreSQL password you want. Feel free to change it. This demonstrates how easy it is to safely handle secrets in distributed applications with Swarm. The docker secret command should succeed and pr int the random identifier that Docker assigned to manage the secret. 

You can verify that the secret was created by listing the secrets in your cluster: `docker secret ls --format "table {{.ID}} {{.Name}} {{.CreatedAt}}"`

## How do I get the images

It is important to get the images before deploying, you can do this by simply running `make install && make load` which will install postgres:9.6.6 and load docker image that has the api. Normally you would build this image with Dockerfile, but because dockerinaction is ancient and their images havent been exactly properly been sure to last, this will have to do.

## Deployment

* Prerequisites: postgres:9.6.6, "the api image"

When you have all the prerequisites, run `make deploy` to deploy the application.


# Outline of the Microservices

> For the sake of trying to preserve some simplicity and keep the dataset more managable, this microservice only deals with a single country.

<p>To start the request, the client first needs to authenticate with Keycloak to get an access token. Once the token is obtained, the client makes a request to the Spring Cloud API Gateway. The API Gateway service is the entry point to our entire architecture; this service communicates with the Eureka service discovery to retrieve the locations of the consumer, reservation and hotel service and then calls the specific microservice.</p>
<pre></pre>
<p>Once the request arrives at the consumer service, it validates the access token against Keycloak to see if the user has permission to continue the process. Once validated, the consumer service updates and retrieves its information from the database and sends it back to the client as an HTTP response.</p>

<ol>
  <li>(Hotel Service locates and select a hotel available for the desired arrival and departure dates.</li>
  <li>(Hotel Service) Select an offer to stay in the hotel at a specific price in a particular type of a room. This offer may also include meals or other add-ons.</li>
  <li>(Customer-service, Reservation-service and Authentication-service) Identify the guest (this may already be done if the guest is logged into the system) and means of guaranteeing the reservation.</li>
  <li>(Reservation-service and Customer-service) Complete the reservation and issue a reservation number.</li>
</ol>

<ol>
  <li>Client inputs the country and city, Customer-service calls to geoservice to fetch hotels from that area</li>
</ol>

## Outline

* Client (Typescript, React)

<p>The client for the application.</p>

* Configuration Server (Spring Cloud Config Server)

<p>Segregates and centralizes application configuration data from the application. Maintains appliation properties for all the services, acts as a centralized configuration. Allows to set up the application properties with environment-specific values, uses Hashicorp Vault configuration repository to store the applicatoin properties and encrypts the sensitive property files using symmetric encryption.</p>

* HashiCorp Vault (Hashicorp Vault)

<p>Configuration server uses Vault as the backend to securely access the properties. Vault is configured through docker-compose.yml and it is mounted at ./vault where it picks up configuration.</p>

* RedHat Keycloak (Authentication and Authorization) 

<p>Handles everything related to user authentication and authorization, sign up / sign in / sign out.</p>

* Service Registry (Spring Cloud Eureka)

<p>Service discovery is implemented through Spring Cloud Eureka. Services register themselves to the registry, other services can obtain the location of a service instance by querying the service registry, which knows locations of all service instances.</p>

* Distributed Tracing Server (Zipkin)

<p>Distributed tracing is implemented through Zipkin, Spring Cloud Sleuth and ELK Stack to provide log correlation, log aggregation and tracing. server for the application, provides monitoring across all of the microservices.</p>

* Hotel Service (Apache Cassandra)

1. Finds hotels based on location of the city. 
2. Plans and handles everything regarding hotels.
3. Handles payments by customers.
4. Handles Bookings by customers.

Note: Some of these tasks could have been partitioned to yet another service, but to keep this project in some sort of sensible scale (whatever that is to me) I decided to cutdown the complexity by having Hotel-service and Customer-service perform more actions.

* Customer Service (MariaDB)

1. Stores customer information.
2. Handles customer invoices.

### Notes on Spring Security

Using MVC matchers instead of ant matchers due to MVC matchers intepreting the mappings for the paths to endpoints the same way as Spring itself. Whereas ant matchers match exactly the given expression, since they aren't aware of Spring MVC functionality, which can lead to some unintended unauthorized paths.

Do not add multiple filters at the same position in the chain.

Using Apache Cassandra for built-in clustering, eventual consistency, and linear write scalability, making it good candidate for service oriented architecture.

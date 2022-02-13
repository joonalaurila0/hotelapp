# Hotel application

This is a microservices based hotel booking application, implemented with Java and Spring "ecosystem". For the sake of keeping application scale in sensible size for demonstration project, it only deals with a single country, rather than searching for hotels worldwide. The application is structured into different services handling differnet parts of the application, this includes the client, configuration server, service discovery, gateway api, customer service, hotel service, reservation service and the client. On top of this application is using Keycloak server for it's authentication and authorization, Hashicorp Vault for handling secrets, Zipkin for distributed tracing, and ELK stack for logging aggregation and Prometheus and Grafana for monitoring metrics. Apache Cassandra for the hotel and reservation service and MariaDB for the customer service to deal with persistence.

# How to run

```console
$ git clone https://github.com/oscarl0000/hotelapp
$ cd hotelapp
$ TO BE ADDED
```

# Directory structure

TO BE ADDED

# Lisence

# Hotel application

This is a microservices based hotel booking application, implemented with Java Spring "ecosystem". For the sake of keeping application scale in sensible size for demonstration project, it only deals with a single country, rather than searching for hotels worldwide. The application is structured into different services handling differenet parts of the application, this includes the Client, Configuration server, Service Discovery, API Gateway, HashiCorp Vault for handling secrets, RedHat Keycloak for authorization, Apache Kafka for event streaming platform, ELK stack with Zipkin for Distributed tracing, Customer service, Hotel service. On top of this application is using Keycloak server for it's authentication and authorization, Hashicorp Vault for handling secrets, Zipkin for distributed tracing, and ELK stack for logging aggregation Prometheus and Grafana for monitoring metrics, Apache Cassandra for the Hotel service and MariaDB for the Customer service to deal with persistence.

# How to run

```console
$ git clone https://github.com/oscarl0000/hotelapp
$ cd hotelapp
$ TO BE ADDED
```

# Directory structure

TO BE ADDED

# Lisence

TO BE ADDED

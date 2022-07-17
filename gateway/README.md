# About Gateway

Service routing is done through Spring Cloud Gateway, to provide single entry point for all of the services. Implements API Gateway pattern, all the traffic routes through API Gateway and the gateway will route the requests to the appropriate services.

Acts as the centralized Policy Enforment Point of the project, that handles inbound traffic to microservice calls. Essentially operates as a reverse proxy between the client and the services.

# Security implementation

Gateway enforces OAuth 2.0 as the security protocol to secure the APIs it exposes at the edge. Only authenticated clients are granted access to the services.

Note that gateway creates automated route mappings based on the eureka service ids, but if there are no service instances are running, the gateway will not exposes the routes.
[More about automated mappings](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-discoveryclient-route-definition-locator)

[Actuator API](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#actuator-api)

*Authentication and authorization — Because all service calls route through a service gateway, the service gateway is a natural place to check whether the callers of a service have authenticated themselves.*

*Metric collection and logging — A service gateway can be used to collect metrics and log information as a service call passes through it. You can also use the service gateway to confirm that critical pieces of information are in place for user requests, thereby ensuring that logging is uniform. This doesn’t mean that you shouldn’t collect metrics from within your individual services. Rather, a service gateway allows you to centralize the collection of many of your basic metrics, like the number of times the service is invoked and the service response times.*

# Making calls to services

To make a call to the Hotel-service to get all the cities as an example: `curl http://127.0.0.1:8072/hotel/cities/all`. The service name acts as the key for the service gateway to look up the location of the service.

## More examples:

* `curl http://localhost:8072/customer-service/customers/all | jq .`
* `curl http://localhost:8072/hotel-service/cities/all | jq .`

# How to see all the mapped routes?

[Link](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#recap-the-list-of-all-endpoints)

To see all the routes managed by the gateway api: `curl http://localhost:8072/actuator/gateway/routes`

# Dynamically refreshing the routes

Spring Actuator exposes a POST-based endpoint route, actuator/gateway/ refresh, that will cause it to reload its route configuration.

# Configuration properties

[Link](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/appendix.html)

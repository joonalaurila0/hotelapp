# Instance endpoint to see the registered instances

For example, hotel-service with application ID of 'hotel', which is derived from the spring.application.name, would have endpoint: `http://localhost:8761/eureka/apps/hotel`

# Eureka dashboard

Eureka dashboard can be sed to view services from `http://localhost:8761`

**NOTE: When a service registers with Eureka, Eureka waits for three successive health checks over the course of 30 seconds before the service becomes available. You can change the period by setting eureka.instance.leaseRenewalIntervalInSeconds, by default this is 30 seconds.**

[EurekaInstanceConfigBean](https://github.com/spring-cloud/spring-cloud-netflix/blob/main/spring-cloud-netflix-eureka-client/src/main/java/org/springframework/cloud/netflix/eureka/EurekaInstanceConfigBean.java)
[EurekaClientConfigBean](https://github.com/spring-cloud/spring-cloud-netflix/blob/main/spring-cloud-netflix-eureka-client/src/main/java/org/springframework/cloud/netflix/eureka/EurekaClientConfigBean.java)

# Registering with Eureka

From: [1.2 Registering with Eureka](https://cloud.spring.io/spring-cloud-netflix/multi/multi__service_discovery_eureka_clients.html)
> When a client registers with Eureka, it provides meta-data about itself — such as host, port, health indicator URL, home page, and other details. Eureka receives heartbeat messages from each instance belonging to a service. If the heartbeat fails over a configurable timetable, the instance is normally removed from the registry.

Every service registered with Eureka will have the application ID and the instance ID. The application ID represents a group service instance. 

# default zone / defaultZone

> defaultZone is a magic string fallback value that provides the service URL for any client that does not express a preference (in other words, it is a useful default).


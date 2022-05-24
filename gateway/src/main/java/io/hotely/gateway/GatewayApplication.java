package io.hotely.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableEurekaClient
@Configuration
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

  //@Bean
  //public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
  //  return builder.routes()
  //    .route(route -> 
  //        // Customer-Service Routes
  //        route.path("/bookings").and().path("/customers")
  //        .and().path("/invoices").and().path("/rooms")
  //        .and().method("GET", "POST", "PUT", "DELETE")
  //        .and().uri("http://127.0.0.1/8000"))
  //        // Hotel-Service Routes
  //    .route(route ->
  //        route.path("/cities").and().path("/hotels").and().path("/rooms")
  //        .and().path("/bookings").and().path("/reviews")
  //        .and().method("GET", "POST", "PUT", "DELETE")
  //        .and().uri("http://127.0.0.1/8001"))
  //    .build();
  //}
}

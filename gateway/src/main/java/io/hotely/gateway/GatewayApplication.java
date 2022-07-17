package io.hotely.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@Configuration
@RestController
public class GatewayApplication {
  @Value(value = "${kc-provider-conf}")
  private String providerUri;

	public static void main(String[] args) {
    SpringApplication.run(GatewayApplication.class, args);
	}
}

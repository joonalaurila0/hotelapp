package io.hotely.discovery;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscoveryApplication.class, args);
	}

  @Bean
  ApplicationRunner applicationRunner(Environment env) {
    return args -> {
      System.out.println("/* ***************************************** */");
      System.out.println("   Configuration Server running at " + env.getProperty("server.port"));
      System.out.println("   Profile: " + env.getProperty("spring.profiles.active"));
      System.out.println("   Eureka instance hostname: " + env.getProperty("eureka.instance.hostname"));
      System.out.println("   Eureka default zone: " + env.getProperty("eureka.client.serviceUrl.defaultZone"));
      System.out.println("/* ***************************************** */");
      System.out.println("   Test for config values: " + env.getProperty("test.data"));
      System.out.println("   Test for config values: " + env.getProperty("test.data2"));
      System.out.println("/* ***************************************** */");
    };
  }
}
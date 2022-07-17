package io.hotely.hotel;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@RefreshScope
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "io.hotely.hotel")
public class HotelApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelApplication.class, args);
  }

  @Bean
  @Profile("dev") // Does not run on 'default' profile
  CommandLineRunner testingDB(Environment env) {
    return args -> {
      System.out.println("/* ***************************************** */");
      System.out.println("   App: " + env.getProperty("spring.application.name"));
      System.out.println("   Profile: " + env.getProperty("spring.profiles.active"));
      System.out.println("/* ***************************************** */");
      System.out.println("   THIS IS SOLELY FOR DEVELOPMENT PURPOSES:");
      System.out.println("   Database credentials: " + env.getProperty("spring.data.cassandra.username") + ", " + env.getProperty("spring.data.cassandra.password"));
      System.out.println("   Keyspace: " + env.getProperty("spring.data.cassandra.keyspace-name"));
      System.out.println("   Local Datacenter: " + env.getProperty("spring.data.cassandra.local-datacenter"));
      System.out.println("   contact-points: " + env.getProperty("spring.data.cassandra.contact-points"));
      System.out.println("   DB Port: " + env.getProperty("spring.data.cassandra.port"));
      System.out.println("/* ***************************************** */");
    };
  }
}

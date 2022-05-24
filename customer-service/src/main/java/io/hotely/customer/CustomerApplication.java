package io.hotely.customer;

import java.sql.Timestamp;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.hotely.customer.repositories.CustomerRepository;

@RefreshScope
@EnableEurekaClient
@SpringBootApplication
public class CustomerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerApplication.class, args);
	}

  @Bean
  CommandLineRunner testingDB(Environment env) {
    return args -> {
      System.out.println("/* ***************************************** */");
      System.out.println("   App: " + env.getProperty("spring.application.name"));
      System.out.println("   Profile: " + env.getProperty("spring.profiles.active"));
      System.out.println("/* ***************************************** */");
      System.out.println("   THIS IS SOLELY FOR TESTING PURPOSES:");
      System.out.println("   Database credentials: " + env.getProperty("spring.data.cassandra.username") + ", " + env.getProperty("spring.data.cassandra.password"));
      System.out.println("   Keyspace: " + env.getProperty("spring.data.cassandra.keyspace-name"));
      System.out.println("   Local Datacenter: " + env.getProperty("spring.data.cassandra.local-datacenter"));
      System.out.println("   contact-points: " + env.getProperty("spring.data.cassandra.contact-points"));
      System.out.println("   DB Port: " + env.getProperty("spring.data.cassandra.port"));
      System.out.println("/* ***************************************** */");
      System.out.println("   THIS IS SOLELY FOR TESTING PURPOSES:");
      System.out.println("   test: " + env.getProperty("test.data"));
      System.out.println("   test: " + env.getProperty("test.data2"));
      System.out.println("/* ***************************************** */");
    };
  }
}

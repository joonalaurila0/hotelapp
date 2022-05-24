package io.hotely.hotel;

import java.util.UUID;

import com.datastax.oss.driver.api.core.CqlSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RefreshScope
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class HotelApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelApplication.class, args);

    //CqlSession cqlSession = CqlSession.builder().withKeyspace("hotelapp").build();

    //CassandraOperations template = new CassandraTemplate(cqlSession);

    //Example jonDoe = template.insert(newExample("Jon Doe", 40));

    //log.info(template.selectOne(Query.query(Criteria.where("id").is(jonDoe.getId())), Example.class).getId().toString());
    //template.truncate(Example.class);

    //cqlSession.close();
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

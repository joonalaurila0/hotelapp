package io.hotely.hotel;

import java.util.UUID;

import com.datastax.oss.driver.api.core.CqlSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.hotely.hotel.Example;
import io.hotely.hotel.VaultCredentials;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@EnableFeignClients
@EnableConfigurationProperties(VaultCredentials.class)
public class HotelApplication {

  private final VaultCredentials configuration;

  public HotelApplication(VaultCredentials configuration) {
    this.configuration = configuration;
  }

  private static final Logger log = LoggerFactory.getLogger(HotelApplication.class);

  @Autowired
  HelloClient client;

  @Value("${spring.application.name}")
  private String appName;

  private static Example newExample(String name, int age) {
    return new Example(UUID.randomUUID().toString(), name, age);
  }

  @RequestMapping("/")
  public String hello() {
    return client.hello();
  }

	public static void main(String[] args) {
		SpringApplication.run(HotelApplication.class, args);

    //CqlSession cqlSession = CqlSession.builder().withKeyspace("hotelapp").build();

    //CassandraOperations template = new CassandraTemplate(cqlSession);

    //Example jonDoe = template.insert(newExample("Jon Doe", 40));

    //log.info(template.selectOne(Query.query(Criteria.where("id").is(jonDoe.getId())), Example.class).getId());

    //template.truncate(Example.class);
    //cqlSession.close();
    System.out.println("Yeet");
	}

  @Bean
  CommandLineRunner testingDB(Environment env) {
    return args -> {
      System.out.println("/* ***************************************** */");
      System.out.println("   Configuration Server running at " + env.getProperty("server.port"));
      System.out.println("   Profile: " + env.getProperty("spring.profiles.active"));
      System.out.println("   App: " + env.getProperty("spring.application.name"));
      System.out.println("   Spring Cloud Config: " + env.getProperty("spring.cloud.config.enabled"));
      System.out.println("/* ***************************************** */");
      System.out.println("   THIS IS SOLELY FOR TESTING PURPOSES:");
      System.out.println("   Database credentials: " + env.getProperty("spring.data.cassandra.username") + ", " + env.getProperty("spring.data.cassandra.password"));
      System.out.println("   Keyspace: " + env.getProperty("spring.data.cassandra.keyspace-name"));
      System.out.println("   Local Datacenter: " + env.getProperty("spring.data.cassandra.local-datacenter"));
      System.out.println("   contact-points: " + env.getProperty("spring.data.cassandra.contact-points"));
      System.out.println("   DB Port: " + env.getProperty("spring.data.cassandra.port"));
      System.out.println("/* ***************************************** */");
      System.out.println("   Vault uri: " + env.getProperty("spring.cloud.vault.uri"));
      System.out.println("   Vault scheme: " + env.getProperty("spring.cloud.vault.scheme"));
      System.out.println("   Vault auth: " + env.getProperty("spring.cloud.vault.authentication"));
      System.out.println("   Vault token: " + env.getProperty("spring.cloud.vault.token"));
      System.out.println("/* ***************************************** */");
      System.out.println("   THIS IS SOLELY FOR TESTING PURPOSES:");
      System.out.println("   test: " + env.getProperty("test.data"));
      System.out.println("   test: " + env.getProperty("test.data2"));
      System.out.println("   cassuser: " + env.getProperty("cassuser"));
      System.out.println("   casspass: " + env.getProperty("casspass"));
      System.out.println("/* ***************************************** */");
      System.out.println("   cassandra.username: " + configuration.getUsername());
      System.out.println("   cassandra.password: " + configuration.getPassword());
      System.out.println("   vault creds: " + configuration.toString());
      System.out.println("/* ***************************************** */");
    };
  }

  @FeignClient("hotel")
  interface HelloClient {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    String hello();
  }

}

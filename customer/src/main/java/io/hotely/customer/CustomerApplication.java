package io.hotely.customer;

import java.sql.Timestamp;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import io.hotely.customer.entities.Customer;
import io.hotely.customer.entities.Invoice;
import io.hotely.customer.entities.VaultCredentials;
import io.hotely.customer.repositories.CustomerRepository;
import io.hotely.customer.services.HotelService;

@SpringBootApplication
@EnableConfigurationProperties(VaultCredentials.class)
public class CustomerApplication {

  private static final Logger logger = LoggerFactory.getLogger(CustomerApplication.class);
  private final VaultCredentials configuration;

  public CustomerApplication(VaultCredentials configuration) {
    this.configuration = configuration;
  }

	public static void main(String[] args) {
		SpringApplication.run(CustomerApplication.class, args);
	}

  @Bean
  CommandLineRunner testingDB(HotelService hotelService, Environment env) {
    return args -> {
      System.out.println("/* ***************************************** */");
      System.out.println("   Server running at " + env.getProperty("server.port"));
      System.out.println("   Profile: " + env.getProperty("spring.profiles.active"));
      System.out.println("   App: " + env.getProperty("spring.application.name"));
      System.out.println("   Spring Cloud Config: " + env.getProperty("spring.cloud.config.enabled"));
      System.out.println("/* ***************************************** */");
      System.out.println("   THIS IS SOLELY FOR TESTING PURPOSES:");
      System.out.println("   Database credentials: " + env.getProperty("spring.datasource.username") + ", " + env.getProperty("spring.datasource.password"));
      System.out.println("   Database url: " + env.getProperty("spring.datasource.url"));
      System.out.println("   Database driver: " + env.getProperty("spring.datasource.driver-class-name"));
      System.out.println("/* ***************************************** */");
      System.out.println("   DDL-AUTO: " + env.getProperty("spring.jpa.hibernate.ddl-auto"));
      System.out.println("   Show SQL: " + env.getProperty("spring.jpa.show-sql"));
      System.out.println("   Hibernate Dialect: " + env.getProperty("spring.jpa.properties.hibernate.dialect"));
      System.out.println("   JPA Open in view: " + env.getProperty("spring.jpa.open-in-view"));
      System.out.println("/* ***************************************** */");
      System.out.println("   THIS IS SOLELY FOR TESTING PURPOSES:");
      System.out.println("   test: " + env.getProperty("test.data"));
      System.out.println("   test: " + env.getProperty("test.data2"));
      System.out.println("/* ***************************************** */");
      System.out.println("   THIS IS SOLELY FOR TESTING PURPOSES:");
      System.out.println("   http2: " + env.getProperty("server.http2.enabled"));
      System.out.println("   compression: " + env.getProperty("server.compression.enabled"));
      System.out.println("   logging root: " + env.getProperty("logging.level.root"));
      System.out.println("   logging spring web: " + env.getProperty("logging.level.org.springframework.web"));
      System.out.println("   logging hibernate: " + env.getProperty("logging.level.org.hibernate"));
      System.out.println("/* ***************************************** */");
      System.out.println("   mariadb.username: " + configuration.getUsername());
      System.out.println("   mariadb.password: " + configuration.getPassword());
      System.out.println("   vault: " + configuration.toString());
      System.out.println("/* ***************************************** */");
      System.out.println("\n");
      Timestamp ts = new Timestamp(System.currentTimeMillis());
      UUID customerId = UUID.fromString("1cf87586-aa7d-4432-81c8-941cc9745a3a");
      UUID invoiceId = UUID.fromString("f3574e83-bd0b-4b03-953b-7d6bdc91d2ae");
      Invoice newInvoice = new Invoice(12.0, ts, false, false);
      //hotelService.addInvoice(customerId, newInvoice);
      System.out.println("\n");
      Customer result = hotelService.findById(customerId);

      //hotelService.fetchCustomerInvoices(customerId);
      //hotelService.updateInvoicePaidStatus(true, invoiceId);
      //hotelService.updateInvoiceCanceledStatus(true, invoiceId);

      //System.out.println(hotelService.fetchCustomerInvoices(customerId));
      //System.out.println(hotelService.updateInvoice(customerId, newInvoice));
    };
  }

}

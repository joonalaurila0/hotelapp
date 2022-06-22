package io.hotely.hotel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import javax.inject.Inject;

import com.datastax.oss.driver.api.core.CqlSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.hotely.hotel.config.LocalDateToDate;
import io.hotely.hotel.entities.Booking;
import io.hotely.hotel.entities.BookingStatus;
import io.hotely.hotel.repositories.BookingRepository;

@RefreshScope
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "io.hotely.hotel")
public class HotelApplication {

  @Inject
  private BookingRepository bookingRepository;

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(HotelApplication.class, args);

      LocalDateToDate localDateToDate = new LocalDateToDate();
      
     GenericConversionService genericConversionService = ctx.getBean(GenericConversionService.class);
     genericConversionService.addConverter(localDateToDate);

    String[] beans = ctx.getBeanDefinitionNames();

    // File that stores the initialized beans
    File file = new File("InitBeans.txt");
    String filePath = file.getAbsolutePath();
    System.out.println("File path: " + filePath);

    for (String bean : beans) { 
      try {
        file.createNewFile();
        if (file.exists())
          Files.write(Paths.get(filePath), bean.concat("\n").getBytes(), StandardOpenOption.APPEND);
      } catch (IOException e) { e.printStackTrace(); }
      System.out.println("Bean initialized: " + bean); 
    }
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

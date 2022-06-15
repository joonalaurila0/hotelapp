package io.hotely.gateway;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Paths;

import javax.management.MBeanServer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder.JwkSetUriReactiveJwtDecoderBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sun.management.HotSpotDiagnosticMXBean;

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

  @RequestMapping("/message")
  public String index() {
    return "secret message";
  }

  private static void dumpHeap(String outputFile, boolean live) {
    File file = new File(outputFile);
    if (file.exists()) {
      file.delete();
    }
    MBeanServer server = ManagementFactory.getPlatformMBeanServer();
    try {
      HotSpotDiagnosticMXBean mxBean = ManagementFactory.newPlatformMXBeanProxy(server, "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class);
      mxBean.dumpHeap(outputFile, live);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //@Bean 
  //public ReactiveJwtDecoder jwtDecoder() {
  //    return NimbusReactiveJwtDecoder.withJwkSetUri(providerUri).build();
  //}

  @Bean
  CommandLineRunner run(Environment env) {
    return args -> {
      System.out.println("PROVIDER URI: " + providerUri);
      dumpHeap("dump.hprof", true);
    };
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

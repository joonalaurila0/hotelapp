package io.hotely.config;

import java.util.Set;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableConfigServer
public class ConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigApplication.class, args);
	}

  @Bean
  @Profile("dev") // Don't run by default and from test(s)
  ApplicationRunner applicationRunner(Environment env, EnvironmentRepository envRepo) throws Exception {
    String hostenv = env.getProperty("spring.cloud.config.server.vault.host");
    return args -> {
      System.out.println("/* ***************************************** */");
      System.out.println("   Configuration Server running at " + env.getProperty("server.port"));
      System.out.println("   Profile: " + env.getProperty("spring.profiles.active"));
      System.out.println("   Vault port: " + env.getProperty("spring.cloud.config.server.vault.port"));
      System.out.println("   Vault host: " + hostenv);
      System.out.println("   Vault backend: " + env.getProperty("spring.cloud.config.server.vault.backend"));
      System.out.println("   Vault defaultKey: " + env.getProperty("spring.cloud.config.server.vault.defaultKey"));
      System.out.println("   Vault authentication: " + env.getProperty("spring.cloud.config.server.vault.authentication"));
      System.out.println("   Vault token: " + env.getProperty("spring.cloud.config.server.vault.token"));
      System.out.println("/* ***************************************** */");
      System.out.println("/* ***************************************** */");
    };
  }
}

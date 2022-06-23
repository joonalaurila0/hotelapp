package io.hotely.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebFluxSecurity
public class AppConfig {

  @Bean
  public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
    http.authorizeExchange((auth) -> auth
        .pathMatchers(HttpMethod.GET, "/message/**").hasAuthority("SCOPE_message:read")
        .pathMatchers(HttpMethod.GET, "/hotel-service/**").permitAll()
        .pathMatchers(HttpMethod.GET, "/actuator/**").permitAll()
        .and()
        .authorizeExchange()
        .anyExchange().authenticated()
        )
      .oauth2ResourceServer((resourceServer) -> resourceServer
          .jwt(withDefaults())
          .and().cors().disable().csrf().disable()
          );
    return http.build();
  }
}


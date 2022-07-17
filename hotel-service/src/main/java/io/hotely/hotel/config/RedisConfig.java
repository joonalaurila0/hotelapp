package io.hotely.hotel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

  @Value("${redis.hostname}")
  private String redisHostname;

  @Value("${redis.port}")
  private Integer redisPort;

  // Setup redis API
  @Bean
  JedisConnectionFactory jedisConnectionFactory() {
    String hostname = redisHostname;
    Integer port = redisPort;

    RedisStandaloneConfiguration redisStandaloneConfiguration 
      = new RedisStandaloneConfiguration(hostname, port);

    return new JedisConnectionFactory(redisStandaloneConfiguration);
  }

  // Redis Data Access API
  @Bean
  public RedisTemplate<Integer, Object> redisTemplate() {
    RedisTemplate<Integer, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(jedisConnectionFactory());
    return template;
  }
}

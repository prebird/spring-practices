package com.example.messagepractice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimpleTutorialConfig {
  @Bean
  public Queue hello() {
    return new Queue("hello");
  }
}

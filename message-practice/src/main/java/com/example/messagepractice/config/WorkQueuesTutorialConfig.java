package com.example.messagepractice.config;

import com.example.messagepractice.consumer.WorkQueuesTutorialConsumer;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkQueuesTutorialConfig {
  @Bean
  public Queue workQueueQueue() {
    return new Queue("workQueueQueue");
  }

  @Bean
  public WorkQueuesTutorialConsumer WorkQueuesTutorialConsumer1() {
    return new WorkQueuesTutorialConsumer(1);
  }

  @Bean
  public WorkQueuesTutorialConsumer WorkQueuesTutorialConsumer2() {
    return new WorkQueuesTutorialConsumer(2);
  }
}

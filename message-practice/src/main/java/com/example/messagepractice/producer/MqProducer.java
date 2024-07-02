package com.example.messagepractice.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MqProducer {
  private static final String EXCHANGE_NAME = "sample.exchange";

  private final RabbitTemplate rabbitTemplate;

  @GetMapping
  public String samplePublish() {
    rabbitTemplate.convertAndSend(EXCHANGE_NAME, "sample.routing.#", "RabbitMQ + SpringBoot = Success");
    return "Message sending!";
  }
}

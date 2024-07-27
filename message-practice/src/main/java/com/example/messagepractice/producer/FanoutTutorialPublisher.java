package com.example.messagepractice.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FanoutTutorialPublisher {
  private final RabbitTemplate template;
  private final FanoutExchange fanoutExchange;

  @GetMapping("/fanout/{message}")
  public void publishFanoutMessage(@PathVariable String message) {
    template.convertAndSend(fanoutExchange.getName(), "", message);
    log.info(">>> [fanout] publish message complete : {}", message);
  }
}

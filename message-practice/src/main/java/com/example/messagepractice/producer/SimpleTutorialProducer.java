package com.example.messagepractice.producer;

import com.example.messagepractice.BookDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring AMQP 는 다른 스프링 프레임워크 기능 처럼 RabbitMQ를 사용하기 위한 boilerplate 코드를 줄여준다.
 * RabbitTemplate 이 좋은 예이다. convertAndSend로 큐의 이름과 메세지를 주입하면 전송이된다.
 */

@Slf4j
@RequiredArgsConstructor
@RestController
public class SimpleTutorialProducer {
  private final RabbitTemplate rabbitTemplate;
  private final Queue hello;

  @GetMapping("/simpleTutorial")
  public void send() {
    String message = "Hello World!";
    this.rabbitTemplate.convertAndSend(hello.getName(), message);
    log.info(" [simpleTutorial] Send: {}", message);
  }
}

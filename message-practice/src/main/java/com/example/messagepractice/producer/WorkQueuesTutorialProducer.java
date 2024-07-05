package com.example.messagepractice.producer;

import com.example.messagepractice.BookDto;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


/**
 * `RabbitTemplate.convertAndSend()` : java 객체를 메세지로 변환한 후, default exchange 와 default routing key 로 전송
 */

@Slf4j
@RequiredArgsConstructor
@RestController
public class WorkQueuesTutorialProducer {
  private final RabbitTemplate rabbitTemplate;
  private final Queue workQueueQueue;

  private AtomicLong idSequence = new AtomicLong();

  @GetMapping("/WorkQueuesTutorialProducer/send/{repeat}")
  public void send(@PathVariable Integer repeat) throws InterruptedException {
    for (int rep = 0 ; rep < repeat; rep++) {
      BookDto bookDto = new BookDto(idSequence.addAndGet(1), "new book..", 10000);
      rabbitTemplate.convertAndSend(workQueueQueue.getName(), bookDto);
      log.info("[WorkQueuesTutorialProducer] Sent {}", bookDto);
    }
  }
}

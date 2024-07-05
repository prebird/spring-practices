package com.example.messagepractice.consumer;

import com.example.messagepractice.BookDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Slf4j
//@Component
@RabbitListener(queues = "workQueueQueue")
public class WorkQueuesTutorialConsumer {
  private final int instance;

  public WorkQueuesTutorialConsumer(int instance) {
    this.instance = instance;
  }

  @RabbitHandler
  public void receive(BookDto bookDto) throws InterruptedException {
    log.info("[instance {}] >>> receiving, {}", instance, bookDto);
    log.info("[instance {}] sleep 3 seconds (book id {} processing...)", instance, bookDto.getId());
    Thread.sleep(3000);
    log.info("instance {} [WorkQueuesTutorialConsumer] book id {} finished", instance, bookDto.getId());
  }
}

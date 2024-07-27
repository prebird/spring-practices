package com.example.messagepractice.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


/**
 * RabbitListener 어노테이션에 주입받은 큐의 이름을 할당할 수 없어서
 * 익명큐가 선언된 빈의 이름을 SPEL 으로 명시
 * 다른 방법이 있을것 같다.
 */
@Component
@Slf4j
public class FanoutTutorialConsumer {

  @RabbitListener(queues = "#{autoDeleteQueue1.name}")
  public void receive1(String in) throws InterruptedException {
    receiveAndLog(in, 1);
  }

  @RabbitListener(queues = "#{autoDeleteQueue2.name}")
  public void receive2(String in) throws InterruptedException {
    receiveAndLog(in, 2);
  }


  public void receiveAndLog(String message, int index) {
    log.info(">>> [fanout consumer {}]: {}", index ,message);
  }
}

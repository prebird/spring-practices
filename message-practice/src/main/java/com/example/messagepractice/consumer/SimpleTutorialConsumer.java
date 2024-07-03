package com.example.messagepractice.consumer;

import com.example.messagepractice.BookDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * RabbitListener 에는 큐이름을 명시한다.
 * 메서드는 아래와 같은 형식으로 전송된다.
 * GenericMessage [payload=byte[44], headers={amqp_receivedDeliveryMode=PERSISTENT, amqp_receivedRoutingKey=hello, amqp_contentEncoding=UTF-8, amqp_deliveryTag=1, amqp_consumerQueue=hello, amqp_redelivered=false, id=d4652113-02a2-8825-0004-1fe7246bd6d6, amqp_consumerTag=amq.ctag-s8oPWoSPyhCsAC4HuFRTlA, amqp_lastInBatch=false, contentType=application/json, __TypeId__=com.example.messagepractice.BookDto, timestamp=1720013447631}]
 *
 * MessageConverter(https://docs.spring.io/spring-amqp/reference/amqp/message-converters.html)
 * byte[] 타입으로 전송되는 메세지를 객체에 매핑하기 위한 설정을 해주지 않으면 {@link org.springframework.messaging.converter.MessageConversionException} 예외가 발생한다.
 *
 *
 *
 * 에러 핸들링
 * 또한, 에러가 발생하면 자체적으로 Error Handler 가 동작하는 것 같다.
 * ingErrorHandler$DefaultExceptionStrategy : Fatal message conversion error; message rejected; it will be dropped or routed to a dead letter exchange, if so configured
 * 위 에러메세지를 보아, DefaultExceptionStrategy (기본 에러 처리 전략)에 따라 Dead Letter Exchange 로 라우팅 되는 것 같다.
 */
@Slf4j
@Component
public class SimpleTutorialConsumer {

  @RabbitListener(queues = "hello")
  public void receive(String in) {
    log.info("[SimpleTutorial] Receive: {}", in);
  }
}

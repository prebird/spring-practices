package com.example.messagepractice.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 레빗엠큐 + 스프링 AMQP 사용시 설정
 *
 * 1. MessageConverter
 * 메세지 컨버터를 설정하지 않으면 아래 에러가 표출된다. 관련 공식 문서는 (https://docs.spring.io/spring-amqp/reference/amqp/message-converters.html#java-deserialization) 이다.
 * Caused by: java.lang.SecurityException: Attempt to deserialize unauthorized class com.example.lab06.entity.User; add allowed class name patterns to the message converter or, if you trust the message orginiator, set environment variable 'SPRING_AMQP_DESERIALIZATION_TRUST_ALL' or system property 'spring.amqp.deserialization.trust.all' to true
 * 원인은 Default Message Converter 인 {@link SimpleMessageConverter}를 사용하는 경우, 불문명한 출처의 객체를 deserialize 하는 것을 막기위해 허용 객체를 설정해 주어야하기 때문이다.
 * SimpleMessageConverter는 text/plain 타입의 메세지를 기본으로 받고 자세한 설명은 https://docs.spring.io/spring-amqp/reference/amqp/message-converters.html#simple-message-converter 에 있다.
 *
 * 일반적으로 Json 타입의 객체를 받는 경우, {@link Jackson2JsonMessageConverter}를 빈으로 등록하면 된다.
 *
 */
@Configuration
public class RabbitMqConfig {
  @Bean
  public MessageConverter messageConverter() {
    Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
    return jackson2JsonMessageConverter;
  }
}

package com.example.actuator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.audit.InMemoryAuditEventRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ActuatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActuatorApplication.class, args);
	}

	@Bean
	public InMemoryHttpExchangeRepository httpExchangeRepository() {
		// 최대 요청수 100개, 개발 단계에서 사용하면 편함, 운영에서는 모니터링 툴, 핀포인트, zipkin 등 다른 기술을 사용하는 것이 좋다.
		return new InMemoryHttpExchangeRepository();
	}
}

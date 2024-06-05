package com.example.cachepractice;

import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

@EnableCaching
@SpringBootApplication
public class CachePracticeApplication {

  public static void main(String[] args) {
    SpringApplication.run(CachePracticeApplication.class, args);
  }
}

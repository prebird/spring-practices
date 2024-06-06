package com.example.cachepractice.food.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaffeineCacheConfig {
  @Bean
  public CacheManager cacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager();

    // default 설정 등록
    Caffeine<Object, Object> caffeine = Caffeine.newBuilder().recordStats()
        .expireAfterWrite(1, TimeUnit.SECONDS);
    cacheManager.setCaffeine(caffeine);

    // custom 설정 등록
    cacheManager.registerCustomCache("allFoods",
        Caffeine.newBuilder()
        .recordStats()
        .expireAfterWrite(60, TimeUnit.SECONDS)
        .maximumSize(100)
        .build());

    return cacheManager;
  }
}

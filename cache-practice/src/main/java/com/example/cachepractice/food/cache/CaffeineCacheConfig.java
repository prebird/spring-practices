package com.example.cachepractice.food.cache;

import com.example.cachepractice.food.dto.FoodDto;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.cache.CaffeineStatsCounter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class CaffeineCacheConfig {
  private final MeterRegistry meterRegistry;
  @Bean
  public CacheManager cacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager();

    // default 설정 등록
    Caffeine<Object, Object> caffeine = Caffeine.newBuilder().recordStats()
        .expireAfterWrite(1, TimeUnit.SECONDS);
    cacheManager.setCaffeine(caffeine);

//     custom 설정 등록
    Arrays.stream(CaffeineCacheEnum.values()).forEach(
        cacheEnum -> cacheManager.registerCustomCache(cacheEnum.getName(), cacheEnum.getCache()));

    ;
    // 카페인 캐시 추가 메트릭 설정
    cacheManager.registerCustomCache("allFoods", Caffeine.newBuilder()
        .recordStats(() -> new CaffeineStatsCounter(meterRegistry, "allFoods")) // 카페인 캐시 추가 메트릭 설정
        .expireAfterWrite(60, TimeUnit.SECONDS)
        .evictionListener((Object key, Object value,
            RemovalCause cause) ->
            log.info(String.format(
                "Key %s was evicted (%s)%n", key, cause)))
        .build());

    return cacheManager;
  }
}

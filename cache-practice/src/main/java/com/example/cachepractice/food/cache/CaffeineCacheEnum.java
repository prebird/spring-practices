package com.example.cachepractice.food.cache;

import com.example.cachepractice.food.dto.FoodDto;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import io.micrometer.core.instrument.binder.cache.CaffeineStatsCounter;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Getter
public enum CaffeineCacheEnum {
  ALL_FOODS("allFoods",
      Caffeine.newBuilder()
      .recordStats()
      .expireAfterWrite(60, TimeUnit.SECONDS)
      .evictionListener((Object key, Object value,
          RemovalCause cause) ->
          log.info(String.format(
              "Key %s was evicted (%s)%n", key, cause)))
      .weigher((String key, List<FoodDto> value) -> value.size())
      .maximumWeight(100)
      .build());

  private final String name;
  private final Cache cache;
}

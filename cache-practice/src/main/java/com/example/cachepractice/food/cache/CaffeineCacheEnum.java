package com.example.cachepractice.food.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
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
      .maximumSize(100)
      .evictionListener((Object key, Object value,
          RemovalCause cause) ->
          log.info(String.format(
              "Key %s was evicted (%s)%n", key, cause)))
      .build());

  private final String name;
  private final Cache cache;
}

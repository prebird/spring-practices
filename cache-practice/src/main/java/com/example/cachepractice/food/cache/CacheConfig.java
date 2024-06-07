package com.example.cachepractice.food.cache;

import com.example.cachepractice.food.dto.FoodDto;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class CacheConfig {

  @Bean
  public TimedAspect timedAspect(MeterRegistry meterRegistry) {
    return new TimedAspect(meterRegistry);
  }

  @Bean
  public MeterBinder allFoodsCacheSize(CacheManager cacheManager) {
    return registry -> Gauge.builder("cachepractice.cache.allFoods.size", cacheManager, manager -> {
      List<FoodDto> foodDtos = extractValueFromCacheManager(manager);
      return getSize(foodDtos);
    }).register(registry);
  }

  private static int getSize(List<FoodDto> foodDtos) {
    if (foodDtos == null) {
      return 0;
    }
    return foodDtos.size();
  }

  @Bean
  public MeterBinder allFoodsCacheWeight(CacheManager cacheManager) {
    return registry -> Gauge.builder("cachepractice.cache.allFoods.weight", cacheManager, manager -> {
      List<FoodDto> foodDtos = extractValueFromCacheManager(manager);
      int size = getSize(foodDtos);
      if (size == 0) {
        return 0;
      }
      return estimateWeight(foodDtos);
    }).register(registry);
  }

  public long estimateWeight(List<FoodDto> foodDtos) {
    return foodDtos.size() * calculateObjectSize(foodDtos.get(0));
  }

  public static long calculateObjectSize(Object obj) {
    try {
      try (ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream()) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream)) {
          objectOutputStream.writeObject(obj);
          objectOutputStream.flush();
          return byteOutputStream.size();
        }
      }
    } catch (IOException e) {
      return 0;
    }
  }

  private List<FoodDto> extractValueFromCacheManager(CacheManager manager) {
    Cache cache = manager.getCache("allFoods");
    ValueWrapper valueWrapper = cache.get("getAllFood");
    if (valueWrapper != null) {
      return (List<FoodDto>) valueWrapper.get();
    }
    return null;
  }
}

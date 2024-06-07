package com.example.cachepractice.food.cache;

import com.example.cachepractice.food.dto.FoodDto;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import java.util.ArrayList;
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
      if (foodDtos == null) {
        return 0;
      }
      return foodDtos.size();
    }).register(registry);
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

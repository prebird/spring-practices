package com.example.cachepractice;

import com.example.cachepractice.food.domain.Food;
import com.example.cachepractice.food.domain.FoodRepository;
import com.example.cachepractice.food.domain.FoodType;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {
  private final FoodRepository foodRepository;
  private final CacheManager cacheManager;

  @Override
  public void run(String... args) throws Exception {
    for (int i = 0; i < 10; i++) {
      createFood("testfood" + i, 1000);
    }
    log.info("cacheManager : {}", cacheManager.getClass().getSimpleName());
  }

  void createFood(String name, Integer price) {
    foodRepository.saveAndFlush(Food.builder()
            .name(name)
            .price(price)
            .createdAt(LocalDateTime.now())
            .foodType(FoodType.FRUIT)
        .build());
  }
}

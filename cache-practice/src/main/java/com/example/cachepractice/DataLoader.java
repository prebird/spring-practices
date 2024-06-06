package com.example.cachepractice;

import com.example.cachepractice.food.domain.Food;
import com.example.cachepractice.food.domain.FoodRepository;
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
    createFood("치킨", 10000);
    createFood("피자", 12000);
    createFood("자장면", 3000);
    createFood("양장피", 20000);
    for (int i = 0; i < 1000; i++) {
      createFood("testfood" + i, 1000);
    }
    log.info("cacheManager : {}", cacheManager.getClass().getSimpleName());
  }

  void createFood(String name, Integer price) {
    foodRepository.saveAndFlush(Food.builder()
            .name(name)
            .price(price)
        .build());
  }
}

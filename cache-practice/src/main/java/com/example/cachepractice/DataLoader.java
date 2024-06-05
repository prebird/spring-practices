package com.example.cachepractice;

import com.example.cachepractice.food.domain.Food;
import com.example.cachepractice.food.domain.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {
  private final FoodRepository foodRepository;

  @Override
  public void run(String... args) throws Exception {
    createFood("치킨", 10000);
    createFood("피자", 12000);
    createFood("자장면", 3000);
    createFood("양장피", 20000);
  }

  void createFood(String name, Integer price) {
    foodRepository.saveAndFlush(Food.builder()
            .name(name)
            .price(price)
        .build());
  }
}

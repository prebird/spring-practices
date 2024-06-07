package com.example.cachepractice.food;

import com.example.cachepractice.food.dto.FoodDto;
import io.micrometer.core.annotation.Timed;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/foods")
@RestController
public class FoodController {
  private final FoodService foodService;

  @Timed("cachepractice.cache.getAllFoodList")
  @GetMapping
  public List<FoodDto> getAllFoodList() {
    log.info("getAllFoodList api called");
    return foodService.getAllFood();
  }

  @GetMapping("/evict")
  public void evictAllFoods() {
    foodService.evictAllFoods();
  }
}

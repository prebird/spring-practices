package com.example.cachepractice.food;

import com.example.cachepractice.food.dto.FoodDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/foods")
@RestController
public class FoodController {
  private final FoodService foodService;

  @GetMapping
  public List<FoodDto> getAllFoodList() {
    return foodService.getAllFood();
  }
}

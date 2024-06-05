package com.example.cachepractice.food.dto;

import com.example.cachepractice.food.domain.Food;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor @Builder
@Getter
public class FoodDto {
  private Long id;
  private String name;
  private Integer price;

  public static FoodDto from (Food food) {
    return FoodDto.builder()
        .id(food.getId())
        .name(food.getName())
        .price(food.getPrice())
        .build();
  }
}

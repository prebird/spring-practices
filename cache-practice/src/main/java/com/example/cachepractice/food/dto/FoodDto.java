package com.example.cachepractice.food.dto;

import com.example.cachepractice.food.domain.Food;
import com.example.cachepractice.food.domain.FoodType;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
@Getter
public class FoodDto implements Serializable {
  private Long id;
  private String name;
  private Integer price;
  private LocalDateTime createdAt;
  private FoodType foodType;

  public static FoodDto from (Food food) {
    return FoodDto.builder()
        .id(food.getId())
        .name(food.getName())
        .price(food.getPrice())
        .createdAt(food.getCreatedAt())
        .foodType(food.getFoodType())
        .build();
  }
}

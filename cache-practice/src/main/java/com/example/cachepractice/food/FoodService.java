package com.example.cachepractice.food;

import com.example.cachepractice.food.domain.FoodRepository;
import com.example.cachepractice.food.dto.FoodDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class FoodService {
  private final FoodRepository foodRepository;

  public List<FoodDto> getAllFood() {
    return foodRepository.findAll().stream()
        .map(FoodDto::from).toList();
  }
}

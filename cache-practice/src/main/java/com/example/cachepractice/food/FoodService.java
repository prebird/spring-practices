package com.example.cachepractice.food;

import com.example.cachepractice.food.domain.FoodRepository;
import com.example.cachepractice.food.dto.FoodDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FoodService {
  private final FoodRepository foodRepository;

  @Cacheable(value="allFoods", key = "#root.methodName")
  public List<FoodDto> getAllFood() {
    log.info("not cached");
    return foodRepository.findAll().stream()
        .map(FoodDto::from).collect(Collectors.toList());
  }

  @Cacheable(value = "getFood", key = "#id")
  public FoodDto getFood(Long id) {
    return FoodDto.from(foodRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("찾을 수 없습니다.")));
  }

  @CacheEvict(value = "allFoods", allEntries = true)
  public void evictAllFoods() {
  }
}

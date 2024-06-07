package com.example.cachepractice.food;

import com.example.cachepractice.food.domain.FoodRepository;
import com.example.cachepractice.food.dto.FoodDto;
import io.micrometer.core.annotation.Timed;
import java.util.List;
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
        .map(FoodDto::from).toList();
  }

  @CacheEvict(value = "allFoods", allEntries = true)
  public void evictAllFoods() {

  }
}

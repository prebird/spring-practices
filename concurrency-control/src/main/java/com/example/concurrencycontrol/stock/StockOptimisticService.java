package com.example.concurrencycontrol.stock;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockOptimisticService {
  private final StockRepository stockRepository;

  @Transactional
  public void decrease(Long id, Long quantity) {
    Stock stock = stockRepository.findByIdWithOptimisticLock(id).orElseThrow();
    stock.decrease(quantity);
  }


}

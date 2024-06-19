package com.example.concurrencycontrol.stock;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class StockOptimisticFacade {
  private final StockOptimisticService stockOptimisticService;

  public void decreaseWithRetryWhenFail(Long id, Long quantity) throws InterruptedException {
    while (true) {
      try {
        stockOptimisticService.decrease(id, quantity);
        break;
      } catch (ObjectOptimisticLockingFailureException e) {
        Thread.sleep(5);
      }
    }
  }

}

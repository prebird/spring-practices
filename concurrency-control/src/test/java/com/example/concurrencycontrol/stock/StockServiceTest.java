package com.example.concurrencycontrol.stock;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StockServiceTest {

  @Autowired
  private StockService stockService;
  @Autowired
  private StockRepository stockRepository;

  @BeforeEach
  public void before() {
    stockRepository.saveAndFlush(new Stock(1L, 100L));
  }

  @AfterEach
  public void after() {
    stockRepository.deleteAll();
  }

  @Test
  void 재고_감소() {
    stockService.decrease(1L, 1L);

    Stock stock = stockRepository.findById(1L).orElseThrow();
    assertThat(stock.getQuantity()).isEqualTo(99L);
  }

  @Test
  void 재고_감소_동시에_100개() throws InterruptedException {
    int threadCount = 100;
    ExecutorService executorService = Executors.newFixedThreadPool(32);
    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      executorService.submit(() -> {
        try {
          stockService.decrease(1L, 1L);
        } finally {
          latch.countDown();
        }
      });
    }
    latch.await();

    Stock stock = stockRepository.findById(1L).orElseThrow();

    // 100 - (1 * 100)
    assertThat(stock.getQuantity()).isEqualTo(0);
  }
}

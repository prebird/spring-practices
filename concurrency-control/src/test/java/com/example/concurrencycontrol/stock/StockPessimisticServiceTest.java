package com.example.concurrencycontrol.stock;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.concurrencycontrol.IntegrationTest;
import com.example.concurrencycontrol.stock.repository.StockMapper;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class StockPessimisticServiceTest extends IntegrationTest {

  @Autowired
  private StockPessimisticService stockPessimisticService;
  @Autowired
  private StockRepository stockRepository;
  @Autowired
  private StockMapper stockMapper;

  private Stock stock1;

  @BeforeEach
  public void before() {
    stock1 = stockRepository.saveAndFlush(new Stock(1L, 100L));
  }

  @AfterEach
  public void after() {
    stockRepository.deleteAll();
  }

  @Test
  void 재고_감소() {
    stockPessimisticService.decreaseWithJpa(stock1.getId(), 1L);

    Stock stock = stockRepository.findById(stock1.getId()).orElseThrow();
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
          stockPessimisticService.decreaseWithJpa(stock1.getId(), 1L);
        } finally {
          latch.countDown();
        }
      });
    }
    latch.await();

    Stock stock = stockRepository.findById(stock1.getId()).orElseThrow();

    // 100 - (1 * 100)
    assertThat(stock.getQuantity()).isEqualTo(0);
  }

  @Test
  void mybatis를_이용한_조회_테스트() {
    Stock stock = stockMapper.findById(stock1.getId());

    assertThat(stock.getId()).isEqualTo(stock1.getId());
  }

  @Test
  void mybatis를_이용한_재고감소() {
    stockPessimisticService.decreaseWithMybatis(stock1.getId(), 1L);

    Stock stock = stockRepository.findById(stock1.getId()).orElseThrow();
    assertThat(stock.getQuantity()).isEqualTo(99L);
  }

  @Test
  void mybatis를_이용한_재고감소_동시에_100개() throws InterruptedException {
    int threadCount = 100;
    ExecutorService executorService = Executors.newFixedThreadPool(4);
    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      executorService.submit(() -> {
        try {
          stockPessimisticService.decreaseWithMybatis(stock1.getId(), 1L);
        } finally {
          latch.countDown();
        }
      });
    }
    latch.await();

    Stock stock = stockRepository.findById(stock1.getId()).orElseThrow();

    // 100 - (1 * 100)
    assertThat(stock.getQuantity()).isEqualTo(0);
  }
}

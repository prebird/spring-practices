package com.example.concurrencycontrol.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

  private final StockRepository stockRepository;

  @Transactional
  @Override
  public void run(String... args) throws Exception {
    stockRepository.save(new Stock(1L, 100L));
  }
}

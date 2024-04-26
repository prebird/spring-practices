package com.example.concurrencycontrol.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StockService {
  private final StockRepository stockRepository;

  @Transactional
  public void decrease(Long id, Long quantity) {
    // 재고 조회
    // 재고 감소
    // 갱신
    Stock stock = stockRepository.findByIdWithPessimisticLock(id).orElseThrow();
    stock.decrease(quantity);
  }
}

package com.example.concurrencycontrol.stock;

import com.example.concurrencycontrol.stock.repository.StockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StockPessimisticService {
  private final StockRepository stockRepository;
  private final StockMapper stockMapper;

  @Transactional
  public void decreaseWithJpa(Long id, Long quantity) {
    Stock stock = stockRepository.findByIdWithPessimisticLock(id).orElseThrow();
    stock.decrease(quantity);
  }

  @Transactional
  public void decreaseWithMybatis(Long id, Long quantity) {
    Stock stock = stockMapper.findById(id);
    if (stock == null) {
      throw new IllegalArgumentException("stock id 가 존재하지 않습니다.");
    }
    stock.decrease(quantity);
    stockMapper.updateQuantity(stock);
  }
}

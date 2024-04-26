package com.example.concurrencycontrol.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class StockController {
  private final StockService stockService;
  @GetMapping("/stock/{id}")
  public void decreaseStock(@PathVariable Long id, @RequestParam Long quantity) {
    stockService.decrease(id, quantity);
  }
}

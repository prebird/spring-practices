package com.example.concurrencycontrol.stock;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface StockRepository extends JpaRepository<Stock, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select s from Stock s where s.id = :id")
  Optional<Stock> findByIdWithPessimisticLock(Long id);

  @Lock(LockModeType.OPTIMISTIC)
  @Query("select s from Stock s where s.id = :id")
  Optional<Stock> findByIdWithOptimisticLock(Long id);
}

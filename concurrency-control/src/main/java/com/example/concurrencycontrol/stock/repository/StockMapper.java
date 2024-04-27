package com.example.concurrencycontrol.stock.repository;

import com.example.concurrencycontrol.stock.Stock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StockMapper {
  @Select("select * from STOCK where id = #{id}")
  Stock findById(@Param("id") Long id);

  Stock findByIdWithPessimisticLock(@Param("id") Long id);

  Long updateQuantity(@Param("stock") Stock stock);
}

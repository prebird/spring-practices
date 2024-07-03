package com.example.messagepractice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class BookDto {
  private Long id;
  private String name;
  private Integer price;
}

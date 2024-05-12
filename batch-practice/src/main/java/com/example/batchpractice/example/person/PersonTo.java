package com.example.batchpractice.example.person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @AllArgsConstructor @Builder @NoArgsConstructor
public class PersonTo {
  private Long id;
  private String name;
  private Integer age;
}

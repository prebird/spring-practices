package com.example.batchpractice.example.person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @AllArgsConstructor @Builder @NoArgsConstructor @Setter // Read 시 기본생성자와 setter 가 필요
public class PersonFrom {
  private Long id;
  private String lastName;
  private String firstName;
  private Integer age;
}

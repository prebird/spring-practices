package com.example.simplepaymentapp.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class PayDto {
  private String result;
  private String message;
}

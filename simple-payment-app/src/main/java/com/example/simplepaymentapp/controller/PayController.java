package com.example.simplepaymentapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/pay")
@RestController
public class PayController {
  @PostMapping
  public ResponseEntity<PayDto> pay(@RequestParam Long memberId, @RequestParam Long totalAmount) {
    return ResponseEntity.ok(PayDto.builder()
            .result("success")
            .message("결제에 성공했습니다.")
        .build());
  }
}

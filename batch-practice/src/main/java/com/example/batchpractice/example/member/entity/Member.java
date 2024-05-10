package com.example.batchpractice.example.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
public class Member {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  @Enumerated(value = EnumType.STRING)
  private MemberStatus memberStatus;
  private LocalDateTime leaveAt;  // 탈퇴 일자
  @Enumerated(value = EnumType.STRING)
  private DeletedYn isDeleted;

  public void delete() {
    this.isDeleted = DeletedYn.Y;
  }
}

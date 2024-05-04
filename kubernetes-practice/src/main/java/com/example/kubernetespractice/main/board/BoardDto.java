package com.example.kubernetespractice.main.board;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BoardDto {
  private Long id;
  private String title;
  private String content;

  public static BoardDto from(Board board) {
    return new BoardDto(board.getId(), board.getTitle(), board.getContent());
  }
}

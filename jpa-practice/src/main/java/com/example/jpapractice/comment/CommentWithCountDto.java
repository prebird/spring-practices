package com.example.jpapractice.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 댓글 내용과 대댓글의 갯수를 같이 조회
 */
@Getter @AllArgsConstructor @Builder
public class CommentWithCountDto {
  private Long id;
  private String content;
  private Integer subCommentCount;
}

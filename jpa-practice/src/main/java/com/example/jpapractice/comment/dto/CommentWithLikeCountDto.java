package com.example.jpapractice.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 댓글의 좋아요갯수와 싫어요 갯수와 함께 조회
 */
@Getter @AllArgsConstructor @Builder
public class CommentWithLikeCountDto {
  private Long id;
  private String content;
  private Long likeCount;
  private Long dislikeCount;
}

package com.example.jpapractice.comment;

import static org.assertj.core.api.Assertions.*;

import com.example.jpapractice.comment.dto.CommentWithLikeCountDto;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommentRepositoryTest {
  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private CommentLikeRepository commentLikeRepository;

  @Test
  void 댓글_저장에_성공() {
    Comment comment1 = saveComment("댓글1", null);
    Comment subComment1 = saveComment("대댓글1", comment1);
    Comment subComment2 = saveComment("대댓글2", comment1);

    assertThat(comment1).isNotNull();
    assertThat(subComment1).isNotNull();
    assertThat(subComment2).isNotNull();
  }

  @Test
  void 대댓글_조회에_성공() {
    Comment comment1 = saveComment("댓글1", null);
    Comment subComment1 = saveComment("대댓글1", comment1);
    Comment subComment2 = saveComment("대댓글2", comment1);

    List<Comment> subComments = commentRepository.findByParentComment(comment1);

    assertThat(subComments).contains(subComment1);
    assertThat(subComments).contains(subComment2);
  }

  @Test
  void 대댓글_갯수와_함께_댓글_목록_조회() {
    Comment comment1 = saveComment("댓글1", null);
    Comment subComment1 = saveComment("대댓글1", comment1);
    Comment subComment2 = saveComment("대댓글2", comment1);

    List<CommentWithCountDto> dtos = commentRepository.findWithSubCountByParentComment(
        comment1);

    assertThat(dtos).hasSize(2);
  }

  @Test
  void 좋아요_갯수와_함께_댓글_목록_조회() {
    // 댓글
    Comment comment1 = saveComment("댓글1", null);
    Comment subComment1 = saveComment("대댓글1", comment1);
    Comment subComment2 = saveComment("대댓글2", comment1);

    // 좋아요 2개 추가
    saveCommentLike(subComment1, LikeType.LIKE);
    saveCommentLike(subComment1, LikeType.LIKE);
    // 싫어요 1개 추가
    saveCommentLike(subComment1, LikeType.DISLIKE);

    List<CommentWithLikeCountDto> commentList = commentRepository.findWithLikeCountByParentComment(comment1);

    assertThat(commentList).hasSize(2);
    assertThat(commentList.get(0).getLikeCount()).isEqualTo(2);
    assertThat(commentList.get(0).getDislikeCount()).isEqualTo(1);
    assertThat(commentList.get(1).getLikeCount()).isEqualTo(0);
    assertThat(commentList.get(1).getDislikeCount()).isEqualTo(0);
  }

  CommentLike saveCommentLike(Comment comment, LikeType likeType) {
    return commentLikeRepository.save(CommentLike.builder()
            .likeType(likeType)
            .comment(comment)
        .build());
  }

  Comment saveComment(String content, Comment parent) {
    return commentRepository.save(Comment.builder()
            .content(content)
            .parentComment(parent)
        .build());
  }
}

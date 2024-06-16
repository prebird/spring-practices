package com.example.jpapractice.comment;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommentRepositoryTest {
  @Autowired
  private CommentRepository commentRepository;

  @Test
  void saveComment_success() {
    Comment comment1 = saveComment("댓글1", null);
    Comment subComment1 = saveComment("대댓글1", comment1);
    Comment subComment2 = saveComment("대댓글2", comment1);

    assertThat(comment1).isNotNull();
    assertThat(subComment1).isNotNull();
    assertThat(subComment2).isNotNull();
  }

  @Test
  void findCommentByParent_success() {
    Comment comment1 = saveComment("댓글1", null);
    Comment subComment1 = saveComment("대댓글1", comment1);
    Comment subComment2 = saveComment("대댓글2", comment1);

    List<Comment> subComments = commentRepository.findByParentComment(comment1);

    assertThat(subComments).contains(subComment1);
    assertThat(subComments).contains(subComment2);
  }

  Comment saveComment(String content, Comment parent) {
    return commentRepository.save(Comment.builder()
            .content(content)
            .parentComment(parent)
        .build());
  }

}

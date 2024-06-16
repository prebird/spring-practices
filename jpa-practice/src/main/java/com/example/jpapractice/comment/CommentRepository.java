package com.example.jpapractice.comment;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  @EntityGraph(attributePaths = {"parentComment"})
  List<Comment> findByParentComment(Comment parentComment);
}

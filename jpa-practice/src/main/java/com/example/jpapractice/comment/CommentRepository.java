package com.example.jpapractice.comment;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  @EntityGraph(attributePaths = {"parentComment"})
  List<Comment> findByParentComment(Comment parentComment);

  // size -> 컬렉션의 갯수를 반환하는 서브쿼리
  @Query("""
   select new com.example.jpapractice.comment.CommentWithCountDto(
   c.id,
   c.content,
   size(c.subComments) 
   ) 
   from Comment c 
   where c.parentComment = :parentComment
""")
  List<CommentWithCountDto> findWithSubCountByParentComment(Comment parentComment);
}

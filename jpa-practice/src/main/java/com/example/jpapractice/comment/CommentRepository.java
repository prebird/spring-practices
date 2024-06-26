package com.example.jpapractice.comment;

import com.example.jpapractice.comment.dto.CommentWithLikeCountDto;
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
   (select count(c1) as count from Comment c1 where c1.parentComment.id = c.id)
   ) 
   from Comment c 
   where c.parentComment = :parentComment
""")
  List<CommentWithCountDto> findWithSubCountByParentComment(Comment parentComment);

  @Query("""
    select new com.example.jpapractice.comment.dto.CommentWithLikeCountDto(
      c.id,
      c.content,
      (select count(cl) as count from CommentLike cl where cl.comment.id = c.id and cl.likeType = com.example.jpapractice.comment.LikeType.LIKE),
      (select count(cl) as count from CommentLike cl where cl.comment.id = c.id and cl.likeType = com.example.jpapractice.comment.LikeType.DISLIKE)
    )
    from Comment c
    where c.parentComment = :parentComment
    order by c.id
  """)
  List<CommentWithLikeCountDto> findWithLikeCountByParentComment(Comment parentComment);
}

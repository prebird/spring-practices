package com.example.batchpractice.jpa.member.entity;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {
  @Query(" select m "
      + " from Member m "
      + " where m.memberStatus = 'LEAVE' "
      + " and m.isDeleted = 'N' "
      + " and m.leaveAt <= :fiveYearAgoFromNow")
  Page<Member> findAllLeaveMemberToDelete(LocalDateTime fiveYearAgoFromNow ,Pageable pageable);

  int countByIsDeleted(DeletedYn deletedYn);
}

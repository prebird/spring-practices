package com.example.batchpractice.jpa.member;

import static org.assertj.core.api.Assertions.*;

import com.example.batchpractice.TestBatchConfig;
import com.example.batchpractice.TestUtils;
import com.example.batchpractice.jpa.member.entity.DeletedYn;
import com.example.batchpractice.jpa.member.entity.Member;
import com.example.batchpractice.jpa.member.entity.MemberRepository;
import com.example.batchpractice.jpa.member.entity.MemberStatus;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes = {TestBatchConfig.class})
@TestPropertySource(properties = "spring.batch.job.name=" + DeleteMemberJobConfiguration.JOB_NAME)
class DeleteMemberJobConfigurationTest {
  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;
  @Autowired
  private MemberRepository memberRepository;
  @MockBean
  private Clock clock;

  @Test
  @DisplayName("탈퇴한 회원 삭제에 성공합니다.")
  void deleteLeaveMemberJob_success(@Autowired Job job) throws Exception {
    // given
    this.jobLauncherTestUtils.setJob(job);
    int memberInuse = 4;  // 미탈퇴 회원
    int memberLeaveUnder5Years = 1;   // 탈퇴 && 5년 이하
    int memberLeaveOver5Years = 2;    // 탈퇴 && 5년 이상
    int deletedMember = 3; // 이미 삭제 처리된 회원

    List<Member> members = new ArrayList<>();
    for (int i = 0; i < memberInuse; i++) {
      members.add(Member.builder()
          .name("test")
          .memberStatus(MemberStatus.INUSE)
          .isDeleted(DeletedYn.N)
          .build());
    }
    for (int i = 0; i < memberLeaveUnder5Years; i++) {
      members.add(Member.builder()
          .name("test")
          .memberStatus(MemberStatus.LEAVE)
          .leaveAt(LocalDateTime.of(2021, 1, 1,0,0))
          .isDeleted(DeletedYn.N)
          .build());
    }
    for (int i = 0; i < memberLeaveOver5Years; i++) {
      members.add(Member.builder()
          .name("test")
          .memberStatus(MemberStatus.LEAVE)
          .leaveAt(LocalDateTime.of(2020, 1, 1,0,0))
          .isDeleted(DeletedYn.N)
          .build());
    }
    for (int i = 0; i < deletedMember; i++) {
      members.add(Member.builder()
          .name("test")
          .memberStatus(MemberStatus.LEAVE)
          .leaveAt(LocalDateTime.of(2020, 1, 1,0,0))
          .isDeleted(DeletedYn.Y)
          .build());
    }
    memberRepository.saveAllAndFlush(members);

    // LocalDateTime.now() 시, 반환할 값 Mocking
    Clock clockAfter5Years = TestUtils.convertLocalDateTimeToClock(LocalDateTime.of(2025, 1, 1, 0, 0));
    BDDMockito.given(clock.instant()).willReturn(clockAfter5Years.instant());
    BDDMockito.given(clock.getZone()).willReturn(clockAfter5Years.getZone());

    // when

    JobExecution jobExecution = jobLauncherTestUtils.launchJob();

    // then
    assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
    int totalDeletedCount = memberRepository.countByIsDeleted(DeletedYn.Y);
    assertThat(totalDeletedCount).isEqualTo(memberLeaveOver5Years + deletedMember);
  }
}

package com.example.batchpractice.example.member;

import com.example.batchpractice.example.member.entity.Member;
import com.example.batchpractice.example.member.entity.MemberRepository;
import jakarta.persistence.EntityManagerFactory;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 탈퇴한 회원을 삭제하기 위한 배치잡
 * 1. 탈퇴한지 5년이 지난 회원 조회
 * 2. 삭제 처리 (삭제 플래그 추가)
 *    삭제 플래그가 추가된 데이터는 DB에서 언제든지 삭제될 수 있습니다.
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty(name = "spring.batch.job.name", havingValue = DeleteMemberJobConfiguration.JOB_NAME)
public class DeleteMemberJobConfiguration {
  private final EntityManagerFactory entityManagerFactory;
  private final MemberRepository memberRepository;
  public static final String JOB_NAME = "deleteLeaveMemberJob";
  private static final int CHUNK_SIZE = 1;
  private final Clock clock;

  @Bean
  public Job deleteLeaveMemberJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new JobBuilder(JOB_NAME, jobRepository)
        .start(deleteMemberStep(jobRepository, transactionManager))
        .build();
  }

  @Bean
  @JobScope
  public Step deleteMemberStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("deleteMemberStep", jobRepository)
        .<Member, Member>chunk(CHUNK_SIZE, transactionManager)
        .reader(leaveMemberReader())
        .processor(setDeleteFlag())
        .writer(saveMemberWriter())
        .build();
  }

  @Bean
  @StepScope
  public RepositoryItemReader<Member> leaveMemberReader() {
    return new RepositoryItemReaderBuilder<Member>()
        .name("leaveMemberReader")
        .repository(memberRepository)
        .pageSize(CHUNK_SIZE)
        .methodName("findAllLeaveMemberToDelete")
        .arguments(LocalDateTime.now(clock).minusYears(5))
        .sorts(Collections.singletonMap("id", Direction.ASC))
        .build();
  }

  @Bean
  @StepScope
  public ItemProcessor<Member, Member> setDeleteFlag() {
    return member -> {
      member.delete();
      return member;
    };
  }

  @Bean
  @StepScope
  public JpaItemWriter<Member> saveMemberWriter() {
    return new JpaItemWriterBuilder<Member>()
        .entityManagerFactory(entityManagerFactory)
        .build();
  }
}

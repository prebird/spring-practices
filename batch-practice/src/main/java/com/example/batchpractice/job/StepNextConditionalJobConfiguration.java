package com.example.batchpractice.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class StepNextConditionalJobConfiguration {
  @Bean(name = "stepNextConditionalJob")
  public Job stepNextConditionalJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new JobBuilder("stepNextConditionalJob", jobRepository)
        .start(conditionStep1(jobRepository, transactionManager))
          .on("FAILED") // 작업 1 실패시
          .to(conditionStep3(jobRepository, transactionManager)) // 작업 3을 실행한다.
          .on("*") // 작업3의 결과와 상관 없이
          .end()  // 종료한다.
        .from(conditionStep1(jobRepository, transactionManager))// 작업 1로 부터
          .on("*")  // 실패가 아닌 모든 결과 시
          .to(conditionStep2(jobRepository,transactionManager))  // 작업2를 실시한다.
          .end()
        .build();
  }

  @Bean
  @JobScope
  public Step conditionStep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("conditionStep1", jobRepository)
        .tasklet(((contribution, chunkContext) -> {
          log.info(">>>> conditionStep1");
          contribution.setExitStatus(ExitStatus.FAILED);  // 실패
          return RepeatStatus.FINISHED;
        }), transactionManager)
        .build();
  }

  @Bean
  @JobScope
  public Step conditionStep2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("conditionStep2", jobRepository)
        .tasklet(((contribution, chunkContext) -> {
          log.info(">>>> conditionStep2");
          return RepeatStatus.FINISHED;
        }), transactionManager)
        .build();
  }

  @Bean
  @JobScope
  public Step conditionStep3(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("conditionStep3", jobRepository)
        .tasklet(((contribution, chunkContext) -> {
          log.info(">>>> conditionStep3");
          return RepeatStatus.FINISHED;
        }), transactionManager)
        .build();
  }

}

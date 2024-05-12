package com.example.batchpractice.example.person;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Person 테이블 마이그레이션 예제
 * Person_from 테이블에서 Person_to 테이블로 데이터를 이동시킵니다.
 * PersonFrom 테이블의 FirstName 과 LastName 컬럼의 데이터를 이어붙혀 PersonTo 의 name 컬럼에 마이그레이션합니다.
 */
@Slf4j
@Configuration
public class PersonMigrationConfiguration {
  private static final String JOB_NAME = "migratePersonJob";
  private static final int CHUNK_SIZE = 3;

  @Bean
  public Job migratePersonJob(JobRepository jobRepository, PlatformTransactionManager transactionManager, DataSource dataSource) {
    return new JobBuilder(JOB_NAME, jobRepository)
        .start(migratePersonStep(jobRepository, transactionManager, dataSource))
        .build();
  }

  @Bean
  @JobScope
  public Step migratePersonStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, DataSource dataSource) {
    return new StepBuilder("migratePersonStep", jobRepository)
        .<PersonFrom, PersonTo>chunk(CHUNK_SIZE, transactionManager)
        .reader(personFromJdbcCursorReader(dataSource))
        .processor(transferEntity())
        .writer(personToWriter(dataSource))
        .build();
  }

  @Bean
  @StepScope
  public JdbcCursorItemReader<PersonFrom> personFromJdbcCursorReader(DataSource dataSource) {
    return new JdbcCursorItemReaderBuilder<PersonFrom>()
        .name("personFromJdbcCursorReader")
        .dataSource(dataSource)
        .fetchSize(CHUNK_SIZE)
        .rowMapper(new BeanPropertyRowMapper<>(PersonFrom.class))
        .sql("SELECT id, last_name, first_name, age FROM person_from")
        .build();
  }

  @Bean
  @StepScope
  public ItemProcessor<PersonFrom, PersonTo> transferEntity() {
    return personFrom -> PersonTo.builder()
                                  .name(personFrom.getLastName() + personFrom.getFirstName())
                                  .age(personFrom.getAge())
                                  .build();
  }

  @Bean
  @StepScope
  public JdbcBatchItemWriter<PersonTo> personToWriter(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<PersonTo>()
        .dataSource(dataSource)
        .sql("INSERT INTO person_to (name, age) VALUES (:name, :age)")
        .beanMapped()
        .build();
  }
}

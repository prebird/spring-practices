package com.example.batchpractice.gsExample;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class JobCompletionNotificationListener implements JobExecutionListener {
  private final JdbcTemplate jdbcTemplate;
  @Override
  public void afterJob(JobExecution jobExecution) {
    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info("##### job complete!!!!");
    }
    jdbcTemplate
        .query("SELECT first_name, last_name FROM people", new DataClassRowMapper<>(Person.class))
        .forEach(person -> log.info("Found <{{}}> in the database.", person));
  }
}

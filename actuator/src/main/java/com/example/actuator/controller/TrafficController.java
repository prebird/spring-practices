package com.example.actuator.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TrafficController {

  @GetMapping("cpu")
  public String cpu() {
    log.info("cpu");
    long value = 0;
    for (int i = 0; i < 1000000000; i++) {
      value++;
    }
    return "ok";
  }

  private static List<String> instances = new ArrayList<>();

  @GetMapping("memory")
  public String memory() {
    log.info("memory");
    for (int i = 0; i < 10000000; i++) {
      instances.add("instance" + i);
    }
    return "ok";
  }

  @Autowired
  DataSource dataSource;

  @GetMapping("connection")
  public String connection() throws SQLException {
    log.info("connection");
    Connection connection = dataSource.getConnection();
    //connection.close(); // close 안함

    return "ok";
  }

  @GetMapping("/errorlog")
  public String error() {
    log.error("errorlog");
    return "ok";
  }
}

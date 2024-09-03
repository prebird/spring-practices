package com.example.cachepractice.food.cache;


import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class RedisCacheConfigObjectMapperTest {
  @Test
  public void objectMapper_visibility_Only_field_설정() throws JsonProcessingException {
    RedisCacheConfig redisCacheConfig = new RedisCacheConfig();
    ObjectMapper objectMapper = redisCacheConfig.objectMapper();
    TestClazz testClazz = new TestClazz("1", "2");

    String json = objectMapper.writeValueAsString(testClazz);
    System.out.println(json);

    assertThat(json).contains("fieldBase");
  }

  private class TestClazz {
    private String fieldBase;
    private String val2;

    public String getVal1() {
      return "getter base";
    }

    public TestClazz (String  constructorBase, String val2) {
      this.fieldBase = constructorBase;
      this.val2 = val2;
    }
  }
}

package com.example.cachepractice.food.cache;


import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisCacheConfig {
  @Value("${spring.redis.host}")
  private String host;
  @Value("${spring.redis.port}")
  private Integer port;

  /**
   * 레디스 커넥션 설정
   * Connectors
   *  - Spring Data Redis 에서 지원하는 자바 Redis Connectors 에는 Jedis와 Lettuce 가 있다. (https://docs.spring.io/spring-data/redis/reference/redis/drivers.html)
   *  - Lettuce 로 구현하였다.
   * ConnectionFactory Modes
   *  - Standalone : 단일 레디스 서버, https://docs.spring.io/spring-data/redis/reference/redis/connection-modes.html#redis:standalone
   *  - Master에 쓰기, Replica에 읽기 : Master, Replica 구조, https://docs.spring.io/spring-data/redis/reference/redis/connection-modes.html#redis:write-to-master-read-from-replica
   *  - Redis Sentinel: Master/Slave 의 고가용성(HA) 구성, https://docs.spring.io/spring-data/redis/reference/redis/connection-modes.html#redis:sentinel
   *  - Redis Cluster: 레디스 클러스터, https://docs.spring.io/spring-data/redis/reference/redis/connection-modes.html#cluster.enable
   * @return
   */
  @Bean
  public LettuceConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
  }

  @Bean
  public RedisTemplate<?, ?> redisTemplate() {
    RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory());
    redisTemplate.setEnableTransactionSupport(true);
    redisTemplate.setKeySerializer(RedisSerializer.string());
    redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper()));
    return redisTemplate;
  }

  @Bean
  public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
    RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
        .disableCachingNullValues() // Null 값 캐시 안함
        .entryTtl(Duration.ZERO)  // TTL 설정 안함
        .serializeKeysWith(SerializationPair.fromSerializer(RedisSerializer.string()))
        .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper())));
    return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory)
        .cacheDefaults(cacheConfig)
        .build();
  }

  /**
   * Redis 값 직렬화/역직렬화를 위한 ObjectMapper
   * 1. 날짜 타입을 LocalDateTime 타입으로 처리하기 위한 설정
   *   - 스프링 3.1.1 부터는 LocalDateTime 처리를 위하 https://github.com/spring-projects/spring-data-redis/issues/2722 로 처리 가능하다.
   * 2. activeDefaultTyping 설정
   *   - 직렬화 시, 객체의 타입을 명시해주는 설정이다.
   *   - ObjectMapper 가 Deserialize(readValue()) 할 때 Class 를 넘겨주어야 하기 때문에 필요하다.
   *   - 클래스가 명시되지 않으면 객체는 기본으로 LinkedHashMap 으로 변환 하다가 오류가 난다.
   *   - [] 의 경우, 리스트, 배열 등 타입이 모호하여 역직렬화시 에러가 발생한다.
   * @return
   */
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    PolymorphicTypeValidator ptv = mapper.getPolymorphicTypeValidator();

    return mapper
        .setVisibility(PropertyAccessor.ALL, Visibility.NONE)
        .setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .registerModule(new JavaTimeModule())
        .activateDefaultTyping(ptv, DefaultTyping.NON_FINAL);
  }
}

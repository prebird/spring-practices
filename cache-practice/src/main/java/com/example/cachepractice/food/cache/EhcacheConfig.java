package com.example.cachepractice.food.cache;

import java.util.List;
import javax.cache.Cache;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;
import org.springframework.cache.CacheManager;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EhcacheConfig {
  @Bean
  public CacheManager cacheManager() {
    JCacheCacheManager jCacheCacheManager = new JCacheCacheManager();
    CachingProvider provider = Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");
    javax.cache.CacheManager cacheManager = provider.getCacheManager();

    // Jcache 설정 -> MutableConfiguration 사용
    MutableConfiguration<String, List> configuration =
        new MutableConfiguration<String, List>()
            .setTypes(String.class, List.class)
            .setStoreByValue(false)   // true 이면 heap에 저장되더라도 Serializable 을 구현해야한다.
            .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_MINUTE));
    cacheManager.createCache("allFoods", configuration);

    jCacheCacheManager.setCacheManager(cacheManager);
    return jCacheCacheManager;
  }
}

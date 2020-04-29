package demo.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import demo.enums.CaffeineCacheEnum;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 尚未定义 CacheManager 类型的 bean 或名为 cacheResolver 的 CacheResolver，
 * 则 Spring Boot 会尝试检测以下提供程序（按指示的顺序）：
 *
 * Generic
 * JCache (JSR-107) (EhCache 3, Hazelcast, Infinispan, and others)
 * EhCache 2.x
 * Hazelcast
 * Infinispan
 * Couchbase
 * Redis
 * Caffeine
 * Simple
 */
@Configuration
@EnableCaching
public class LocalCacheConfig {
    @Bean
    @Primary
    public CacheManager caffeinecacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        //CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        List<CaffeineCache> list = new ArrayList<>();
        for(CaffeineCacheEnum c : CaffeineCacheEnum.values()) {
            list.add(new CaffeineCache(c.name(),
                    Caffeine.newBuilder()
                            .recordStats()
                            .expireAfterWrite(c.getTtl(), TimeUnit.SECONDS)
                            .maximumSize(c.getMaxSize())
                            .build()));

        }
        cacheManager.setCaches(list);
        return cacheManager;
    }
}

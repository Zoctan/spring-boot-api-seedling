package com.zoctan.seedling.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import javax.annotation.Resource;

/**
 * @author Zoctan
 * @date 2018/5/27
 */
@Configuration
public class RedisCacheConfig extends CachingConfigurerSupport {
    private final static Logger log = LoggerFactory.getLogger(RedisCacheConfig.class);

    @Resource
    private JedisConnectionFactory jedisConnectionFactory;

    @Bean
    @Override
    public CacheManager cacheManager() {
        //初始化一个RedisCacheWriter
        final RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(this.jedisConnectionFactory);
        final RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
        //初始化RedisCacheManager
        return new RedisCacheManager(redisCacheWriter, defaultCacheConfig);
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, objects) -> {
            String[] value = new String[1];
            final Cacheable cacheable = method.getAnnotation(Cacheable.class);
            if (cacheable != null) {
                value = cacheable.value();
            }
            final CachePut cachePut = method.getAnnotation(CachePut.class);
            if (cachePut != null) {
                value = cachePut.value();
            }
            final CacheEvict cacheEvict = method.getAnnotation(CacheEvict.class);
            if (cacheEvict != null) {
                value = cacheEvict.value();
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(value[0]);
            for (final Object obj : objects) {
                sb.append(":").append(obj.toString());
            }
            return sb.toString();
        };
    }

    /**
     * 错误处理
     */
    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        return new SimpleCacheErrorHandler() {
            @Override
            public void handleCacheGetError(final RuntimeException exception, final Cache cache, final Object key) {
                log.error("cache : {} , key : {}", cache, key);
                log.error("handleCacheGetError", exception);
                super.handleCacheGetError(exception, cache, key);
            }

            @Override
            public void handleCachePutError(final RuntimeException exception, final Cache cache, final Object key, final Object value) {
                log.error("cache : {} , key : {} , value : {} ", cache, key, value);
                log.error("handleCachePutError", exception);
                super.handleCachePutError(exception, cache, key, value);
            }

            @Override
            public void handleCacheEvictError(final RuntimeException exception, final Cache cache, final Object key) {
                log.error("cache : {} , key : {}", cache, key);
                log.error("handleCacheEvictError", exception);
                super.handleCacheEvictError(exception, cache, key);
            }

            @Override
            public void handleCacheClearError(final RuntimeException exception, final Cache cache) {
                log.error("cache : {} ", cache);
                log.error("handleCacheClearError", exception);
                super.handleCacheClearError(exception, cache);
            }
        };
    }
}

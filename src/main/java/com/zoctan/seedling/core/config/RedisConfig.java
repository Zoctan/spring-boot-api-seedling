package com.zoctan.seedling.core.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.zoctan.seedling.core.cache.MyRedisCacheManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis配置
 *
 * @author Zoctan
 * @date 2018/05/27
 */
@Configuration
public class RedisConfig {
  @Bean
  @ConfigurationProperties(prefix = "spring.redis.jedis.pool")
  public JedisPoolConfig jedisPoolConfig() {
    return new JedisPoolConfig();
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.redis")
  public JedisConnectionFactory jedisConnectionFactory(
      @Qualifier(value = "jedisPoolConfig") final JedisPoolConfig poolConfig) {
    return new JedisConnectionFactory(poolConfig);
  }

  /**
   * 配置 RedisTemplate，配置 key 和 value 的序列化类
   *
   * <p>key 序列化使用 StringRedisSerializer, 不配置的话，key 会出现乱码
   */
  @Bean
  public RedisTemplate redisTemplate(
      @Qualifier(value = "jedisConnectionFactory") final JedisConnectionFactory factory) {
    final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

    // 设置 key 的序列化器为字符串 serializer
    final StringRedisSerializer stringSerializer = MyRedisCacheManager.STRING_SERIALIZER;

    redisTemplate.setKeySerializer(stringSerializer);
    redisTemplate.setHashKeySerializer(stringSerializer);

    // 设置 value 的序列化器为 fastjson serializer
    final GenericFastJsonRedisSerializer fastSerializer = MyRedisCacheManager.FASTJSON_SERIALIZER;

    redisTemplate.setValueSerializer(fastSerializer);
    redisTemplate.setHashValueSerializer(fastSerializer);

    // 如果 KeySerializer 或者 ValueSerializer 没有配置
    // 则对应的 KeySerializer、ValueSerializer 才使用 fastjson serializer
    redisTemplate.setDefaultSerializer(fastSerializer);

    redisTemplate.setConnectionFactory(factory);
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }
}

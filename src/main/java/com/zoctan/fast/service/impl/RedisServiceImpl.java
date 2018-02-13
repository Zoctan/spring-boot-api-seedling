package com.zoctan.fast.service.impl;

import com.alibaba.fastjson.JSON;
import com.zoctan.fast.service.RedisService;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

//@Service
public class RedisServiceImpl implements RedisService {
    @Resource
    private RedisTemplate<String, ?> redisTemplate;

    @Override
    public boolean set(final String key, final String value) {
        return this.redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            final RedisSerializer<String> serializer = this.redisTemplate.getStringSerializer();
            connection.set(serializer.serialize(key), serializer.serialize(value));
            return true;
        });
    }

    @Override
    public String get(final String key) {
        return this.redisTemplate.execute((RedisCallback<String>) connection -> {
            final RedisSerializer<String> serializer = RedisServiceImpl.this.redisTemplate.getStringSerializer();
            final byte[] value = connection.get(serializer.serialize(key));
            return serializer.deserialize(value);
        });
    }

    @Override
    public boolean expire(final String key, final long expire) {
        return this.redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    @Override
    public <T> boolean setList(final String key, final List<T> list) {
        final String value = (String) JSON.toJSON(list);
        return this.set(key, value);
    }

    @Override
    public <T> List<T> getList(final String key, final Class<T> clz) {
        final String json = this.get(key);
        if (json != null) {
            return (List<T>) JSON.parseObject(json, clz);
        }
        return null;
    }

    /**
     * 在key列表头部添加一个Object
     *
     * @param key key
     * @param obj obj
     * @return 执行操作后，列表的长度
     */
    @Override
    public long lPush(final String key, final Object obj) {
        final String value = (String) JSON.toJSON(obj);
        return this.redisTemplate.execute((RedisCallback<Long>) connection -> {
            final RedisSerializer<String> serializer = this.redisTemplate.getStringSerializer();
            return (long) connection.lPush(serializer.serialize(key), serializer.serialize(value));
        });
    }

    /**
     * 在key列表的尾部添加一个Object
     *
     * @param key key
     * @param obj obj
     * @return 执行操作后，列表的长度
     */
    @Override
    public long rPush(final String key, final Object obj) {
        final String value = (String) JSON.toJSON(obj);
        return this.redisTemplate.execute((RedisCallback<Long>) connection -> {
            final RedisSerializer<String> serializer = RedisServiceImpl.this.redisTemplate.getStringSerializer();
            return (long) connection.rPush(serializer.serialize(key), serializer.serialize(value));
        });
    }

    /**
     * @param key key
     * @return 列表的第一个元素(当列表 key 不存在时, 返回 null)
     */
    @Override
    public String lPop(final String key) {
        return this.redisTemplate.execute((RedisCallback<String>) connection -> {
            final RedisSerializer<String> serializer = RedisServiceImpl.this.redisTemplate.getStringSerializer();
            final byte[] res = connection.lPop(serializer.serialize(key));
            return serializer.deserialize(res);
        });
    }
}

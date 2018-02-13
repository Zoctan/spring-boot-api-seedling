package com.zoctan.fast.service;

import java.util.List;

public interface RedisService {

    boolean set(String key, String value);

    String get(String key);

    boolean expire(String key, long expire);

    <T> boolean setList(String key, List<T> list);

    <T> List<T> getList(String key, Class<T> clz);

    long lPush(String key, Object obj);

    long rPush(String key, Object obj);

    String lPop(String key);
}

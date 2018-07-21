package com.zoctan.seedling.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具
 *
 * @author Zoctan
 * @date 2018/05/27
 */
@Component
public class RedisUtils {
    private final static Logger log = LoggerFactory.getLogger(RedisUtils.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    //=============================common============================

    /**
     * 设置缓存失效时间
     *
     * @param key     键
     * @param timeout 时间（秒）
     * @return {Boolean}
     */
    public Boolean setExpire(@NonNull final String key, @NonNull final long timeout) {
        return this.redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 获取缓存失效时间
     *
     * @param key 键
     * @return 时间（秒） 0为永久有效
     */
    public Long getExpire(@NonNull final String key) {
        return this.redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * key 是否存在
     *
     * @param key 键
     * @return {Boolean}
     */
    public Boolean hasKey(@NonNull final String key) {
        return this.redisTemplate.hasKey(key);
    }

    /**
     * 删除缓存
     *
     * @param keys 键
     */
    public Boolean delete(@NonNull final String... keys) {
        if (keys.length == 1) {
            return this.redisTemplate.delete(keys[0]);
        } else {
            return keys.length == this.redisTemplate.delete(Arrays.asList(keys));
        }
    }

    //============================String=============================

    /**
     * 获取普通缓存
     *
     * @param key 键
     * @return 值
     */
    public Object getValue(@NonNull final String key) {
        return this.redisTemplate.opsForValue().get(key);
    }

    /**
     * 设置普通缓存
     *
     * @param key   键
     * @param value 值
     * @return {Boolean}
     */
    public Boolean setValue(@NonNull final String key, @NonNull final Object value) {
        try {
            this.redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置普通缓存
     *
     * @param key     键
     * @param value   值
     * @param timeout 时间（秒） 小于等于0时将设为无限期
     * @return {Boolean}
     */
    public Boolean setValue(@NonNull final String key,
                            @NonNull final Object value,
                            @NonNull final long timeout) {
        try {
            this.redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几（大于0）
     * @return 加上指定值之后 key 的值
     */
    public Long incrementValue(@NonNull final String key, @NonNull final long delta) {
        if (delta > 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return this.redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return 减少指定值之后 key 的值
     */
    public Long decrementValue(@NonNull final String key, @NonNull final long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return this.redisTemplate.opsForValue().increment(key, -delta);
    }

    //================================Map=================================

    /**
     * HashGet
     *
     * @param key  键
     * @param item 项
     * @return 值
     */
    public Object getHash(@NonNull final String key, @NonNull final String item) {
        return this.redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> getHash(@NonNull final String key) {
        return this.redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return {Boolean}
     */
    public Boolean putHash(@NonNull final String key, @NonNull final Map<String, Object> map) {
        try {
            this.redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key     键
     * @param map     对应多个键值
     * @param timeout 时间(秒)
     * @return {Boolean}
     */
    public Boolean putHash(@NonNull final String key,
                           @NonNull final Map<String, Object> map,
                           @NonNull final Long timeout) {
        try {
            this.redisTemplate.opsForHash().putAll(key, map);
            if (timeout > 0) {
                this.setExpire(key, timeout);
            }
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return {Boolean}
     */
    public Boolean putHash(@NonNull final String key,
                           @NonNull final String item,
                           @NonNull final Object value) {
        try {
            this.redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key     键
     * @param item    项
     * @param value   值
     * @param timeout 时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return {Boolean}
     */
    public Boolean putHash(@NonNull final String key,
                           @NonNull final String item,
                           @NonNull final Object value,
                           @NonNull final Long timeout) {
        try {
            this.redisTemplate.opsForHash().put(key, item, value);
            if (timeout > 0) {
                this.setExpire(key, timeout);
            }
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void deleteHash(@NonNull final String key, @NonNull final Object... item) {
        this.redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public Boolean hasKeyHash(@NonNull final String key, @NonNull final String item) {
        return this.redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return 加上指定值之后 key 的值
     */
    public double incrementHash(@NonNull final String key,
                                @NonNull final String item,
                                @NonNull final double by) {
        return this.redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return 减少指定值之后 key 的值
     */
    public double decrementHash(@NonNull final String key,
                                @NonNull final String item,
                                @NonNull final double by) {
        return this.redisTemplate.opsForHash().increment(key, item, -by);
    }

    //============================set=============================

    /**
     * 根据 key 获取 Set 中的所有值
     *
     * @param key 键
     * @return Set<Object>
     */
    public Set<Object> getSet(@NonNull final String key) {
        try {
            return this.redisTemplate.opsForSet().members(key);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据 value 从一个 set 中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在, false 不存在
     */
    public Boolean hasKeySet(@NonNull final String key, @NonNull final Object value) {
        try {
            return this.redisTemplate.opsForSet().isMember(key, value);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long addSet(@NonNull final String key, @NonNull final Object... values) {
        try {
            return this.redisTemplate.opsForSet().add(key, values);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 将set数据放入缓存
     *
     * @param key     键
     * @param timeout 时间(秒)
     * @param values  值 可以是多个
     * @return 成功个数
     */
    public Long addSet(@NonNull final String key,
                       @NonNull final Long timeout,
                       @NonNull final Object... values) {
        try {
            final Long count = this.redisTemplate.opsForSet().add(key, values);
            if (timeout > 0) {
                this.setExpire(key, timeout);
            }
            return count;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return 缓存的长度
     */
    public Long getSetSize(@NonNull final String key) {
        try {
            return this.redisTemplate.opsForSet().size(key);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public Long removeSet(@NonNull final String key,
                          @NonNull final Object... values) {
        try {
            return this.redisTemplate.opsForSet().remove(key, values);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }
    //===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return list缓存的内容
     */
    public List<Object> getList(@NonNull final String key,
                                @NonNull final Long start,
                                @NonNull final Long end) {
        try {
            return this.redisTemplate.opsForList().range(key, start, end);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return list缓存的长度
     */
    public Long getListSize(@NonNull final String key) {
        try {
            return this.redisTemplate.opsForList().size(key);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return list中的值
     */
    public Object getListIndex(@NonNull final String key, @NonNull final Long index) {
        try {
            return this.redisTemplate.opsForList().index(key, index);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return 是否放入成功
     */
    public Boolean pushList(@NonNull final String key, @NonNull final Object value) {
        try {
            this.redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将list放入缓存
     *
     * @param key     键
     * @param value   值
     * @param timeout 时间(秒)
     * @return 是否放入成功
     */
    public Boolean pushList(@NonNull final String key,
                            @NonNull final Object value,
                            @NonNull final Long timeout) {
        try {
            this.redisTemplate.opsForList().rightPush(key, value);
            if (timeout > 0) {
                this.setExpire(key, timeout);
            }
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return 是否放入成功
     */
    public Boolean pushList(@NonNull final String key, @NonNull final List<Object> value) {
        try {
            this.redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将list放入缓存
     *
     * @param key     键
     * @param value   值
     * @param timeout 时间(秒)
     * @return 是否放入成功
     */
    public Boolean pushList(@NonNull final String key,
                            @NonNull final List<Object> value,
                            @NonNull final Long timeout) {
        try {
            this.redisTemplate.opsForList().rightPushAll(key, value);
            if (timeout > 0) {
                this.setExpire(key, timeout);
            }
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据索引修改 list 中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return 是否修改成功
     */
    public Boolean updateListIndex(@NonNull final String key,
                                   @NonNull final Long index,
                                   @NonNull final Object value) {
        try {
            this.redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public Long removeList(@NonNull final String key,
                           @NonNull final Long count,
                           @NonNull final Object value) {
        try {
            return this.redisTemplate.opsForList().remove(key, count, value);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

}
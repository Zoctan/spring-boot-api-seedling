package com.zoctan.seedling.mapper;

import com.zoctan.seedling.core.mapper.MyMapper;
import com.zoctan.seedling.model.User;

import java.util.Map;

/**
 * @author Zoctan
 * @date 2018/5/27
 */
public interface UserMapper extends MyMapper<User> {
    User findByParam(Map<String, Object> param);
}
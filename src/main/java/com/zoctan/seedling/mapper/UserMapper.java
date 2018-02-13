package com.zoctan.seedling.mapper;

import com.zoctan.seedling.core.mapper.MyMapper;
import com.zoctan.seedling.model.User;

import java.util.Map;

public interface UserMapper extends MyMapper<User> {
    User findByParam(Map<String, Object> param);
}
package com.zoctan.fast.mapper;

import com.zoctan.fast.core.mapper.MyMapper;
import com.zoctan.fast.model.User;

import java.util.Map;

public interface UserMapper extends MyMapper<User> {
    User findByParam(Map<String, Object> param);
}
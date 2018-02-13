package com.zoctan.fast.service;

import com.zoctan.fast.core.service.Service;
import com.zoctan.fast.model.User;

import java.util.List;


/**
 * Created by Zoctan on 2018/02/04.
 */
public interface UserService extends Service<User> {
    @Override
    void save(User user);

    @Override
    void update(User user);

    @Override
    User findById(Object id);

    User findByUsername(String username);

    void updateLoginTime(String username);

    boolean verifyPassword(String rawPassword, String encodedPassword);
}

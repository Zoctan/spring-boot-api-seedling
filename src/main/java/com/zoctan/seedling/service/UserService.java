package com.zoctan.seedling.service;

import com.zoctan.seedling.core.service.Service;
import com.zoctan.seedling.model.User;


/**
 * @author Zoctan
 * @date 2018/5/27
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

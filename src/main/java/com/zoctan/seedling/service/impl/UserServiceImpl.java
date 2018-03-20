package com.zoctan.seedling.service.impl;

import com.zoctan.seedling.core.exception.ServiceException;
import com.zoctan.seedling.core.service.AbstractService;
import com.zoctan.seedling.mapper.RoleMapper;
import com.zoctan.seedling.mapper.UserMapper;
import com.zoctan.seedling.mapper.UserRoleMapper;
import com.zoctan.seedling.model.Role;
import com.zoctan.seedling.model.User;
import com.zoctan.seedling.model.UserRole;
import com.zoctan.seedling.service.UserService;
import com.zoctan.seedling.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Zoctan on 2018/02/04.
 */
@Service
@Transactional
@Slf4j
public class UserServiceImpl extends AbstractService<User> implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private PasswordEncoder passwordEncoder;

    private void saveRole(final long id, final String roleName) {
        final Condition condition = new Condition(Role.class);
        condition.createCriteria().andCondition("name = ", roleName);
        final Role role = this.roleMapper.selectByCondition(condition).get(0);
        this.userRoleMapper.insert(
                new UserRole()
                        .setUserId(id)
                        .setRoleId(role.getId()));
    }

    /**
     * 重写save方法，密码加密后再存
     */
    @Override
    public void save(final User user) {
        final User u = this.findByUsername(user.getUsername());
        if (u != null) {
            throw new ServiceException("用户名已存在");
        } else {
            user.setPassword(this.passwordEncoder.encode(user.getPassword()));
            user.setRegisterTime(DateUtil.getNowTimestamp());
            this.userMapper.insertSelective(user);
            log.info("User<{}> id : {}", user.getUsername(), user.getId());
            this.saveRole(user.getId(), "USER");
        }
    }

    /**
     * 重写update方法
     */
    @Override
    public void update(final User user) {
        // 如果修改了密码
        if (user.getPassword() != null) {
            // 密码修改后需要加密
            user.setPassword(this.passwordEncoder.encode(user.getPassword().trim()));
        }
        this.userMapper.updateByPrimaryKeySelective(user);
    }

    private User findByParam(final String column, final Object param) {
        final Map<String, Object> map = new HashMap<>();
        map.put(column, param);
        return this.userMapper.findByParam(map);
    }

    @Override
    public User findById(final Object id) {
        return this.findByParam("id", id);
    }

    @Override
    public User findByUsername(final String username) {
        return this.findByParam("username", username);
    }

    @Override
    public boolean verifyPassword(final String rawPassword, final String encodedPassword) {
        return this.passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public void updateLoginTime(final String username) {
        final Condition condition = new Condition(User.class);
        condition.createCriteria()
                .andCondition("username = ", username);
        final User user = new User();
        user.setLastLoginTime(DateUtil.getNowTimestamp());
        this.userMapper.updateByConditionSelective(user, condition);
    }
}

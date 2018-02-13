package com.zoctan.fast.service.impl;

import com.zoctan.fast.core.service.AbstractService;
import com.zoctan.fast.mapper.UserRoleMapper;
import com.zoctan.fast.model.UserRole;
import com.zoctan.fast.service.UserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by Zoctan on 2018/02/09.
 */
@Service
@Transactional
public class UserRoleServiceImpl extends AbstractService<UserRole> implements UserRoleService {
    @Resource
    private UserRoleMapper userRoleMapper;

}

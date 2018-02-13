package com.zoctan.fast.service.impl;

import com.zoctan.fast.core.service.AbstractService;
import com.zoctan.fast.mapper.RoleMapper;
import com.zoctan.fast.model.Role;
import com.zoctan.fast.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by Zoctan on 2018/02/04.
 */
@Service
@Transactional
public class RoleServiceImpl extends AbstractService<Role> implements RoleService {
    @Resource
    private RoleMapper roleMapper;
}

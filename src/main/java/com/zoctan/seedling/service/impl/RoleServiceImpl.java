package com.zoctan.seedling.service.impl;

import com.zoctan.seedling.core.service.AbstractService;
import com.zoctan.seedling.mapper.RoleMapper;
import com.zoctan.seedling.model.Role;
import com.zoctan.seedling.service.RoleService;
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

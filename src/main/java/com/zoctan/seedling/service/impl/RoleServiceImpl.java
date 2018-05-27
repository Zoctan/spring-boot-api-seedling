package com.zoctan.seedling.service.impl;

import com.zoctan.seedling.core.service.AbstractService;
import com.zoctan.seedling.mapper.RoleMapper;
import com.zoctan.seedling.model.Role;
import com.zoctan.seedling.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author Zoctan
 * @date 2018/5/27
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl extends AbstractService<Role> implements RoleService {
    @Resource
    private RoleMapper roleMapper;
}

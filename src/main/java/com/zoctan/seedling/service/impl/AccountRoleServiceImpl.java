package com.zoctan.seedling.service.impl;

import com.zoctan.seedling.core.service.AbstractService;
import com.zoctan.seedling.mapper.AccountRoleMapper;
import com.zoctan.seedling.model.AccountRole;
import com.zoctan.seedling.service.AccountRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author Zoctan
 * @date 2018/05/27
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountRoleServiceImpl extends AbstractService<AccountRole> implements AccountRoleService {
    @Resource
    private AccountRoleMapper accountRoleMapper;

}

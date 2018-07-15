package com.zoctan.seedling.service.impl;

import com.zoctan.seedling.core.exception.ServiceException;
import com.zoctan.seedling.core.service.AbstractService;
import com.zoctan.seedling.entity.AccountDO;
import com.zoctan.seedling.entity.AccountWithRoleDO;
import com.zoctan.seedling.mapper.AccountMapper;
import com.zoctan.seedling.mapper.RoleMapper;
import com.zoctan.seedling.query.AccountQuery;
import com.zoctan.seedling.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author Zoctan
 * @date 2018/05/27
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountServiceImpl extends AbstractService<AccountDO> implements AccountService {
    private final static Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);
    @Resource
    private AccountMapper accountMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 重写 save 方法，密码加密后再存，并且赋予默认角色
     */
    @Override
    public int save(final AccountDO accountDO) {
        final AccountDO a = this.findBy("name", accountDO.getName());
        if (a != null) {
            throw new ServiceException("账户名已存在");
        }
        // 直接在这里对account的密码加密的话，login就会获得加密过的密码，导致验证错误
        final AccountDO accountToDB = new AccountDO();
        BeanUtils.copyProperties(accountDO, accountToDB);

        accountToDB.setPassword(this.passwordEncoder.encode(accountDO.getPassword()));
        this.accountMapper.insertSelective(accountToDB);
        log.debug("create account<{}> id<{}>", accountToDB.getName(), accountToDB.getId());
        // 新建账户默认角色
        return this.roleMapper.insertDefaultAccountRole(accountToDB.getId());
    }

    @Override
    public boolean updateById(final AccountDO account) {
        // 如果修改了密码
        if (account.getPassword() != null) {
            // 密码修改后需要加密
            account.setPassword(this.passwordEncoder.encode(account.getPassword().trim()));
        }
        return this.accountMapper.updateByPrimaryKeySelective(account) == 1;
    }

    @Override
    public AccountWithRoleDO findByIdWithRole(final Long id) {
        final AccountQuery accountQuery = new AccountQuery();
        accountQuery.setId(id);
        return this.accountMapper.selectByQueryWithRole(accountQuery);
    }

    @Override
    public AccountWithRoleDO findByNameWithRole(final String name) {
        final AccountQuery accountQuery = new AccountQuery();
        accountQuery.setName(name);
        return this.accountMapper.selectByQueryWithRole(accountQuery);
    }

    @Override
    public boolean verifyPassword(final String rawPassword, final String encodedPassword) {
        return this.passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public boolean updateLoginTimeByName(final String name) {
        return this.accountMapper.updateLoginTimeByName(name) == 1;
    }
}

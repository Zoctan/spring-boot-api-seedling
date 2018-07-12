package com.zoctan.seedling.service.impl;

import com.zoctan.seedling.core.exception.ServiceException;
import com.zoctan.seedling.core.service.AbstractService;
import com.zoctan.seedling.mapper.AccountMapper;
import com.zoctan.seedling.mapper.AccountRoleMapper;
import com.zoctan.seedling.mapper.RoleMapper;
import com.zoctan.seedling.model.Account;
import com.zoctan.seedling.model.AccountRole;
import com.zoctan.seedling.model.Role;
import com.zoctan.seedling.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author Zoctan
 * @date 2018/05/27
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountServiceImpl extends AbstractService<Account> implements AccountService {
    private final static Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);
    @Resource
    private AccountMapper accountMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private AccountRoleMapper accountRoleMapper;
    @Resource
    private PasswordEncoder passwordEncoder;

    private int saveRole(final long id, final String roleName) {
        final Condition condition = new Condition(Role.class);
        condition.createCriteria().andCondition("name = ", roleName);
        final Role role = this.roleMapper.selectByCondition(condition).get(0);
        return this.accountRoleMapper.insert(new AccountRole().setAccountId(id).setRoleId(role.getId()));
    }

    /**
     * 重写save方法，密码加密后再存
     */
    @Override
    public int save(final Account account) {
        final Account a = this.findByName(account.getName());
        if (a != null) {
            throw new ServiceException("账户名已存在");
        } else {
            // 这里new一个accountToDB
            // 如果直接对account修改某些属性值，调用方再获取account的属性时会出现脏数据
            // 因为这里的account是一个引用而不是值
            // 比如这里调用了save后还需要login获取token
            // 直接在这里对account的密码加密的话，login就会获得加密过的密码，导致验证错误
            final Account accountToDB = new Account();
            // 将属性值一一复制
            BeanUtils.copyProperties(account, accountToDB);

            accountToDB.setPassword(this.passwordEncoder.encode(account.getPassword()));
            this.accountMapper.insertSelective(accountToDB);
            log.debug("Account<{}> id => {}", accountToDB.getName(), accountToDB.getId());
            return this.saveRole(accountToDB.getId(), "USER");
        }
    }

    /**
     * 重写update方法
     */
    @Override
    public int update(final Account account) {
        // 如果修改了密码
        if (account.getPassword() != null) {
            // 密码修改后需要加密
            account.setPassword(this.passwordEncoder.encode(account.getPassword().trim()));
        }
        return this.accountMapper.updateByPrimaryKeySelective(account);
    }

    private Account findByParam(final String column, final Object param) {
        return this.accountMapper.findByParam(new HashMap<String, Object>(1) {{
            this.put(column, param);
        }});
    }

    @Override
    public Account findById(final Object id) {
        return this.findByParam("id", id);
    }

    @Override
    public Account findByName(final String name) {
        return this.findByParam("name", name);
    }

    @Override
    public boolean verifyPassword(final String rawPassword, final String encodedPassword) {
        return this.passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public void updateLoginTimeByName(final String name) {
        this.accountMapper.updateLoginTimeByName(name);
    }
}

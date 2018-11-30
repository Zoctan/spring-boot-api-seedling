package com.zoctan.seedling.service.impl;

import com.zoctan.seedling.core.response.ResultCode;
import com.zoctan.seedling.core.service.AbstractService;
import com.zoctan.seedling.dto.AccountDTO;
import com.zoctan.seedling.entity.AccountDO;
import com.zoctan.seedling.entity.AccountWithRoleDO;
import com.zoctan.seedling.mapper.AccountMapper;
import com.zoctan.seedling.mapper.RoleMapper;
import com.zoctan.seedling.query.AccountQuery;
import com.zoctan.seedling.service.AccountService;
import com.zoctan.seedling.util.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;

/**
 * @author Zoctan
 * @date 2018/05/27
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountServiceImpl extends AbstractService<AccountDO> implements AccountService {
  @Resource private AccountMapper accountMapper;
  @Resource private RoleMapper roleMapper;
  @Resource private PasswordEncoder passwordEncoder;

  /** 重写 save 方法，密码加密后再存，并且赋予默认角色 */
  @Override
  public void save(final AccountDTO accountDTO) {
    final boolean accountNotExist = this.getBy("name", accountDTO.getName()) == null;
    AssertUtils.asserts(accountNotExist, ResultCode.DUPLICATE_NAME);

    final AccountDO accountDO = accountDTO.convertToDO();

    accountDO.setPassword(this.passwordEncoder.encode(accountDTO.getPassword().trim()));
    final boolean saveSuccuss = this.save(accountDO);
    AssertUtils.asserts(saveSuccuss, ResultCode.SAVE_FAILED, "账户持久化失败");
    log.debug("==> Create Account<{}> Id<{}>", accountDO.getName(), accountDO.getId());
    // 新建账户默认角色
    final boolean saveRoleSuccuss = this.roleMapper.saveAsDefaultRole(accountDO.getId()) == 1;
    AssertUtils.asserts(saveRoleSuccuss, ResultCode.SAVE_FAILED, "账户角色持久化失败");
  }

  @Override
  public void updateByName(final AccountDTO accountDTO) {
    final AccountDO accountDO = accountDTO.convertToDO();
    // 如果修改了密码
    if (StringUtils.isNotBlank(accountDTO.getPassword())) {
      // 密码修改后需要加密
      accountDO.setPassword(this.passwordEncoder.encode(accountDTO.getPassword().trim()));
    }
    // 不能修改账户名
    final String name = accountDO.getName();
    accountDO.setName(null);
    // 按 name 字段更新
    final Condition condition = new Condition(AccountDO.class);
    condition.createCriteria().andCondition("name = ", name);
    final boolean updateSuccuss = this.updateByCondition(accountDO, condition);
    AssertUtils.asserts(updateSuccuss, ResultCode.UPDATE_FAILED, "账户更新失败");
  }

  @Override
  public AccountWithRoleDO getByIdWithRole(final Long id) {
    final AccountQuery accountQuery = AccountQuery.builder().id(id).build();
    return this.accountMapper.getByQueryWithRole(accountQuery);
  }

  @Override
  public AccountWithRoleDO getByNameWithRole(final String name) {
    final AccountQuery accountQuery = AccountQuery.builder().name(name).build();
    return this.accountMapper.getByQueryWithRole(accountQuery);
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

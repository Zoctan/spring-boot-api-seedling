package com.zoctan.seedling.service;

import com.zoctan.seedling.core.service.Service;
import com.zoctan.seedling.entity.AccountDO;
import com.zoctan.seedling.entity.AccountWithRoleDO;

/**
 * @author Zoctan
 * @date 2018/05/27
 */
public interface AccountService extends Service<AccountDO> {
    /**
     * 按账户名查询带有角色信息的账户
     *
     * @param name 账户名
     * @return 账户
     */
    AccountWithRoleDO findByNameWithRole(String name);

    /**
     * 按账户Id查询带有角色信息的账户
     *
     * @param id 账户Id
     * @return 账户
     */
    AccountWithRoleDO findByIdWithRole(Long id);

    /**
     * 更新账户
     *
     * @param accountDO 账户
     * @return 是否更新成功
     */
    boolean updateById(AccountDO accountDO);

    /**
     * 按账户名更新最后一次登录时间
     *
     * @param name 账户名
     * @return 是否更新成功
     */
    boolean updateLoginTimeByName(String name);

    /**
     * 验证账户密码
     *
     * @param rawPassword     原密码
     * @param encodedPassword 加密后的密码
     * @return {boolean}
     */
    boolean verifyPassword(String rawPassword, String encodedPassword);
}

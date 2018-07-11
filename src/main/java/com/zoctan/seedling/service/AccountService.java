package com.zoctan.seedling.service;

import com.zoctan.seedling.core.service.Service;
import com.zoctan.seedling.model.Account;

/**
 * @author Zoctan
 * @date 2018/05/27
 */
public interface AccountService extends Service<Account> {
    /**
     * 按账户名查询账户
     *
     * @param name 账户名
     * @return 账户
     */
    Account findByName(String name);

    /**
     * 按账户名更新最后一次登录时间
     *
     * @param name 账户名
     */
    void updateLoginTimeByName(String name);

    /**
     * 验证账户密码
     *
     * @param rawPassword     原密码
     * @param encodedPassword 加密后的密码
     * @return {boolean}
     */
    boolean verifyPassword(String rawPassword, String encodedPassword);
}

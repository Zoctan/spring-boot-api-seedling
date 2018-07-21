package com.zoctan.seedling.entity;

import java.util.List;

/**
 * 包含角色信息的账户实体
 *
 * @author Zoctan
 * @date 2018/07/15
 */
public class AccountWithRoleDO extends AccountDO {
    /**
     * 账户的角色列表
     */
    private List<RoleDO> roles;

    @Override
    public String toString() {
        return super.toString();
    }

    public List<RoleDO> getRoles() {
        return this.roles;
    }

    public void setRoles(final List<RoleDO> roles) {
        this.roles = roles;
    }
}
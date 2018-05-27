package com.zoctan.seedling.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Zoctan
 * @date 2018/5/27
 */
@Table(name = "user_role")
public class UserRole {
    /**
     * 用户Id
     */
    @Id
    @Column(name = "user_id")
    private Long userId;

    /**
     * 角色Id
     */
    @Column(name = "role_id")
    private Long roleId;

    public UserRole setUserId(final Long userId) {
        this.userId = userId;
        return this;
    }

    public UserRole setRoleId(final Long roleId) {
        this.roleId = roleId;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getRoleId() {
        return roleId;
    }
}
package com.zoctan.seedling.entity;

import java.sql.Timestamp;
import java.util.List;

/**
 * 包含角色信息的账户实体
 *
 * @author Zoctan
 * @date 2018/07/15
 */
public class AccountWithRoleDO {
    /**
     * 账户的角色列表
     */
    private List<RoleDO> roles;

    /**
     * 账户Id
     */
    private Long id;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 账户名
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * 注册时间
     */
    private Timestamp registerTime;

    /**
     * 上一次登录时间
     */
    private Timestamp loginTime;

    @Override
    public String toString() {
        return super.toString();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Timestamp getRegisterTime() {
        return this.registerTime;
    }

    public void setRegisterTime(final Timestamp registerTime) {
        this.registerTime = registerTime;
    }

    public Timestamp getLoginTime() {
        return this.loginTime;
    }

    public void setLoginTime(final Timestamp loginTime) {
        this.loginTime = loginTime;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<RoleDO> getRoles() {
        return this.roles;
    }

    public void setRoles(final List<RoleDO> roles) {
        this.roles = roles;
    }
}
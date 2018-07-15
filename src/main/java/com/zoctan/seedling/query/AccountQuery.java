package com.zoctan.seedling.query;

import java.io.Serializable;

/**
 * 账户查询实体
 *
 * @author Zoctan
 * @date 2018/07/15
 */
public class AccountQuery implements Serializable {
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

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
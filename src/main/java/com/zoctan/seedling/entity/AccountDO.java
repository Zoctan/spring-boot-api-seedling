package com.zoctan.seedling.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author Zoctan
 * @date 2018/05/27
 */
@Table(name = "account")
public class AccountDO {
    /**
     * 账户Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 账户名
     */
    @Column(name = "name")
    private String name;

    /**
     * 密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 注册时间
     */
    @Column(name = "register_time")
    private Timestamp registerTime;

    /**
     * 上一次登录时间
     */
    @Column(name = "login_time")
    private Timestamp loginTime;

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
}
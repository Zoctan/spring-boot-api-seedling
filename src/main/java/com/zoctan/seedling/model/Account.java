package com.zoctan.seedling.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Zoctan
 * @date 2018/05/27
 */
@ApiModel(value = "账户实体")
@Table(name = "account")
public class Account implements Serializable {

    //------------------非数据库字段

    @ApiModelProperty(value = "账户的角色")
    @Transient
    private List<Role> roleList;

    //------------------数据库字段

    @ApiModelProperty(value = "账户Id", readOnly = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ApiModelProperty(value = "邮箱")
    @Email
    @Column(name = "email")
    private String email;

    @ApiModelProperty(value = "账户名")
    @NotEmpty(message = "账户名不能为空")
    @Size(min = 3, message = "账户名长度不能小于3")
    @Column(name = "name")
    private String name;

    @ApiModelProperty(value = "密码")
    @NotEmpty(message = "密码不能为空")
    @Size(min = 6, message = "密码长度不能小于6")
    @Column(name = "password")
    private String password;

    @ApiModelProperty(value = "注册时间")
    @Column(name = "register_time")
    private Timestamp registerTime;

    @ApiModelProperty(value = "上一次登录时间")
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

    public List<Role> getRoleList() {
        return this.roleList;
    }

    public void setRoleList(final List<Role> roleList) {
        this.roleList = roleList;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
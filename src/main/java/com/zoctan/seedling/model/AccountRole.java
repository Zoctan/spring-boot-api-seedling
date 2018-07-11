package com.zoctan.seedling.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Zoctan
 * @date 2018/05/27
 */
@ApiModel(value = "账户与角色一一对应实体")
@Table(name = "account_role")
public class AccountRole implements Serializable {
    @ApiModelProperty(value = "账户Id", readOnly = true)
    @Id
    @Column(name = "account_id")
    private Long accountId;

    @ApiModelProperty(value = "角色Id", readOnly = true)
    @Column(name = "role_id")
    private Long roleId;

    public AccountRole setAccountId(final Long accountId) {
        this.accountId = accountId;
        return this;
    }

    public AccountRole setRoleId(final Long roleId) {
        this.roleId = roleId;
        return this;
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public Long getRoleId() {
        return this.roleId;
    }
}
package com.zoctan.seedling.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Zoctan
 * @date 2018/07/15
 */
@ApiModel(value = "账户登录传输实体")
public class AccountLoginDTO implements Serializable {
    @ApiModelProperty(value = "账户名", example = "admin")
    @NotEmpty(message = "账户名不能为空")
    @Size(min = 3, message = "账户名长度不能小于3")
    private String name;

    @ApiModelProperty(value = "密码", example = "admin")
    @NotEmpty(message = "密码不能为空")
    @Size(min = 5, message = "密码长度不能小于5")
    private String password;

    @Override
    public String toString() {
        return super.toString();
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
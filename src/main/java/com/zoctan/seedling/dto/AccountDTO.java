package com.zoctan.seedling.dto;

import com.google.common.base.Converter;
import com.zoctan.seedling.entity.AccountDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.BeanUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Zoctan
 * @date 2018/07/14
 */
@ApiModel(value = "账户传输实体")
public class AccountDTO implements Serializable {
    @ApiModelProperty(value = "账户Id", readOnly = true)
    private Long id;

    @ApiModelProperty(value = "邮箱", example = "123@qq.com")
    @Email(message = "邮箱格式不正确")
    private String email;

    @ApiModelProperty(value = "账户名", example = "admin")
    @NotEmpty(message = "账户名不能为空")
    @Size(min = 3, message = "账户名长度不能小于3")
    private String name;

    @ApiModelProperty(value = "密码", example = "admin")
    @NotEmpty(message = "密码不能为空")
    @Size(min = 5, message = "密码长度不能小于5")
    private String password;

    public AccountDO convertToAccountDO() {
        final AccountDTOConvert accountDTOConvert = new AccountDTOConvert();
        return accountDTOConvert.convert(this);
    }

    public AccountDTO convertFor(final AccountDO accountDO) {
        final AccountDTOConvert accountDTOConvert = new AccountDTOConvert();
        return accountDTOConvert.reverse().convert(accountDO);
    }

    private static class AccountDTOConvert extends Converter<AccountDTO, AccountDO> {
        @ParametersAreNonnullByDefault
        @Override
        protected AccountDO doForward(final AccountDTO accountDTO) {
            final AccountDO accountDO = new AccountDO();
            BeanUtils.copyProperties(accountDTO, accountDO);
            return accountDO;
        }

        @ParametersAreNonnullByDefault
        @Override
        protected AccountDTO doBackward(final AccountDO accountDO) {
            final AccountDTO accountDTO = new AccountDTO();
            BeanUtils.copyProperties(accountDO, accountDTO);
            return accountDTO;
        }
    }

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
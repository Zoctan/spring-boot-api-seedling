package com.zoctan.seedling.dto;

import com.zoctan.seedling.core.dto.AbstractConverter;
import com.zoctan.seedling.entity.AccountDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Zoctan
 * @date 2018/07/14
 */
@Data
@ApiModel(value = "账户传输实体")
@EqualsAndHashCode(callSuper = true)
public class AccountDTO extends AbstractConverter<AccountDTO, AccountDO> implements Serializable {
  private static final long serialVersionUID = 1473352811666797847L;

  @ApiModelProperty(value = "账户Id", accessMode = AccessMode.READ_ONLY)
  private Long id;

  @ApiModelProperty(value = "邮箱", example = "123@qq.com")
  @Email(message = "邮箱格式不正确")
  private String email;

  @ApiModelProperty(value = "账户名", example = "admin", accessMode = AccessMode.READ_ONLY)
  @NotEmpty(message = "账户名不能为空")
  @Size(min = 3, message = "账户名长度不能小于3")
  private String name;

  @ApiModelProperty(value = "密码", example = "admin")
  @NotEmpty(message = "密码不能为空")
  @Size(min = 5, message = "密码长度不能小于5")
  private String password;

  @Override
  protected AccountDTO setDTO() {
    return this;
  }
}

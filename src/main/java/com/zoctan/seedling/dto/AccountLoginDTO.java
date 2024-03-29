package com.zoctan.seedling.dto;

import com.zoctan.seedling.core.dto.AbstractConverter;
import com.zoctan.seedling.entity.AccountDO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author Zoctan
 * @date 2018/07/15
 */
@Data
@Schema(name = "账户登录传输实体")
@EqualsAndHashCode(callSuper = true)
public class AccountLoginDTO extends AbstractConverter<AccountLoginDTO, AccountDO>
    implements Serializable {
  private static final long serialVersionUID = 1945186812588516555L;

  @Schema(name = "账户名", example = "admin")
  @NotEmpty(message = "账户名不能为空")
  @Size(min = 3, message = "账户名长度不能小于3")
  private String name;

  @Schema(name = "密码", example = "admin")
  @NotEmpty(message = "密码不能为空")
  @Size(min = 5, message = "密码长度不能小于5")
  private String password;

  @Override
  protected AccountLoginDTO setDTO() {
    return this;
  }
}

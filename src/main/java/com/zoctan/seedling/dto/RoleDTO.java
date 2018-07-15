package com.zoctan.seedling.dto;

import com.google.common.base.Converter;
import com.zoctan.seedling.entity.RoleDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.BeanUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.Serializable;

/**
 * @author Zoctan
 * @date 2018/07/15
 */
@ApiModel(value = "角色传输实体")
public class RoleDTO implements Serializable {
    @ApiModelProperty(value = "角色Id", readOnly = true)
    private Long id;

    @ApiModelProperty(value = "角色名称")
    private String name;

    public RoleDO convertToRoleDO() {
        final RoleDTOConvert roleDTOConvert = new RoleDTOConvert();
        return roleDTOConvert.convert(this);
    }

    public RoleDTO convertFor(final RoleDO roleDO) {
        final RoleDTOConvert roleDTOConvert = new RoleDTOConvert();
        return roleDTOConvert.reverse().convert(roleDO);
    }

    private static class RoleDTOConvert extends Converter<RoleDTO, RoleDO> {
        @ParametersAreNonnullByDefault
        @Override
        protected RoleDO doForward(final RoleDTO roleDTO) {
            final RoleDO roleDO = new RoleDO();
            BeanUtils.copyProperties(roleDTO, roleDO);
            return roleDO;
        }

        @ParametersAreNonnullByDefault
        @Override
        protected RoleDTO doBackward(final RoleDO roleDO) {
            final RoleDTO roleDTO = new RoleDTO();
            BeanUtils.copyProperties(roleDO, roleDTO);
            return roleDTO;
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

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
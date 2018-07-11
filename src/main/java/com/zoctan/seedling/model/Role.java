package com.zoctan.seedling.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Zoctan
 * @date 2018/05/27
 */
@ApiModel(value = "角色实体")
@Table(name = "role")
public class Role implements Serializable {
    @ApiModelProperty(value = "角色Id", readOnly = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ApiModelProperty(value = "角色名称")
    @Column(name = "name")
    private String name;

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
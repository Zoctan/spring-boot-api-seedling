package com.zoctan.seedling.entity;

import javax.persistence.*;

/**
 * 角色实体
 *
 * @author Zoctan
 * @date 2018/05/27
 */
@Table(name = "role")
public class RoleDO {
    /**
     * 角色Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 角色名称
     */
    @Column(name = "name")
    private String name;

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
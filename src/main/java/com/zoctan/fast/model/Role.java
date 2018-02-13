package com.zoctan.fast.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "role")
public class Role {
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
}
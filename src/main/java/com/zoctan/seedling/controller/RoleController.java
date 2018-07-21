package com.zoctan.seedling.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zoctan.seedling.core.response.Result;
import com.zoctan.seedling.core.response.ResultGenerator;
import com.zoctan.seedling.dto.RoleDTO;
import com.zoctan.seedling.entity.RoleDO;
import com.zoctan.seedling.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色控制器
 *
 * @author Zoctan
 * @date 2018/05/27
 */
@PreAuthorize("hasAuthority('ADMIN')")
@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    private RoleService roleService;

    @PostMapping
    public Result add(@RequestBody final RoleDTO roleDTO) {
        final RoleDO role = roleDTO.convertToRoleDO();
        this.roleService.save(role);
        return ResultGenerator.genOkResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable final Long id) {
        this.roleService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PutMapping
    public Result update(@RequestBody final RoleDTO roleDTO) {
        final RoleDO role = roleDTO.convertToRoleDO();
        this.roleService.update(role);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable final Long id) {
        final RoleDO role = this.roleService.findById(id);
        return ResultGenerator.genOkResult(role);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") final Integer page,
                       @RequestParam(defaultValue = "0") final Integer size) {
        PageHelper.startPage(page, size);
        final List<RoleDO> list = this.roleService.findAll();
        final PageInfo<RoleDO> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }
}

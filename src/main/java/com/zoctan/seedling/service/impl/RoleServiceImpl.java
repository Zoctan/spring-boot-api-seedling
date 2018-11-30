package com.zoctan.seedling.service.impl;

import com.zoctan.seedling.core.response.ResultCode;
import com.zoctan.seedling.core.service.AbstractService;
import com.zoctan.seedling.dto.RoleDTO;
import com.zoctan.seedling.entity.RoleDO;
import com.zoctan.seedling.mapper.RoleMapper;
import com.zoctan.seedling.service.RoleService;
import com.zoctan.seedling.util.AssertUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author Zoctan
 * @date 2018/05/27
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl extends AbstractService<RoleDO> implements RoleService {
  @Resource private RoleMapper roleMapper;

  @Override
  public void save(final RoleDTO roleDTO) {
    final RoleDO role = roleDTO.convertToDO();
    final boolean saveSuccuss = this.save(role);
    AssertUtils.asserts(saveSuccuss, ResultCode.SAVE_FAILED, "角色持久化失败");
  }

  @Override
  public void update(final RoleDTO roleDTO) {
    final RoleDO role = roleDTO.convertToDO();
    final boolean updateSuccuss = this.update(role);
    AssertUtils.asserts(updateSuccuss, ResultCode.UPDATE_FAILED, "角色更新失败");
  }
}

package com.zoctan.seedling.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zoctan.seedling.core.cache.CacheExpire;
import com.zoctan.seedling.core.jwt.JwtUtil;
import com.zoctan.seedling.core.response.Result;
import com.zoctan.seedling.core.response.ResultGenerator;
import com.zoctan.seedling.dto.AccountDTO;
import com.zoctan.seedling.dto.AccountLoginDTO;
import com.zoctan.seedling.entity.AccountDO;
import com.zoctan.seedling.entity.AccountWithRoleDO;
import com.zoctan.seedling.service.AccountService;
import com.zoctan.seedling.service.impl.UserDetailsServiceImpl;
import com.zoctan.seedling.util.JsonUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Zoctan
 * @date 2018/07/15
 */
@Slf4j
@Api(value = "账户接口")
@Validated
@RestController
@RequestMapping("/account")
public class AccountController {
  @Resource private AccountService accountService;
  @Resource private UserDetailsServiceImpl userDetailsService;
  @Resource private JwtUtil jwtUtil;

  @ApiOperation(value = "账户注册", notes = "根据账户信息注册")
  @PostMapping
  public Result register(
      @ApiParam(required = true) @RequestBody @Valid final AccountDTO accountDTO,
      final BindingResult bindingResult) {
    // 账户持久化
    this.accountService.save(accountDTO);
    // 签发 token
    final UserDetails userDetails =
        this.userDetailsService.loadUserByUsername(accountDTO.getName());
    final String token = this.jwtUtil.sign(accountDTO.getName(), userDetails.getAuthorities());
    return ResultGenerator.genOkResult(token);
  }

  @ApiOperation(value = "账户登录")
  @PostMapping("/token")
  public Result login(
      @ApiParam(required = true) @RequestBody @Valid final AccountLoginDTO accountLoginDTO,
      final BindingResult bindingResult) {
    // {"name":"admin","password":"admin"}
    final String name = accountLoginDTO.getName();
    final String password = accountLoginDTO.getPassword();
    // 验证账户
    final UserDetails userDetails = this.userDetailsService.loadUserByUsername(name);
    if (!this.accountService.verifyPassword(password, userDetails.getPassword())) {
      return ResultGenerator.genFailedResult("密码错误");
    }
    // 更新登录时间
    this.accountService.updateLoginTimeByName(name);
    final String token = this.jwtUtil.sign(name, userDetails.getAuthorities());
    return ResultGenerator.genOkResult(token);
  }

  @ApiOperation(value = "账户注销")
  @DeleteMapping("/token")
  public Result logout(@AuthenticationPrincipal final UsernamePasswordAuthenticationToken user) {
    this.jwtUtil.invalidRedisToken(user.getName());
    return ResultGenerator.genOkResult();
  }

  @PreAuthorize("#accountDTO.name == authentication.name or hasAuthority('ADMIN')")
  @ApiOperation(value = "更新账户信息", notes = "根据账户信息更新")
  @PatchMapping
  public Result update(@ApiParam(required = true) @RequestBody final AccountDTO accountDTO) {
    this.accountService.updateByName(accountDTO);
    return ResultGenerator.genOkResult();
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @ApiOperation(
      value = "删除账户",
      notes = "根据 URL 上的 id 删除指定对象",
      authorizations = @Authorization(value = "ADMIN"))
  @ApiImplicitParam(
      name = "id",
      value = "账户Id",
      required = true,
      dataType = "Long",
      paramType = "query")
  @DeleteMapping("/{id}")
  public Result delete(@PathVariable final Long id) {
    this.accountService.deleteById(id);
    return ResultGenerator.genOkResult();
  }

  @ApiOperation(value = "获取账户信息")
  @ApiImplicitParam(name = "id", value = "账户Id", required = true, dataType = "Long")
  @GetMapping("/{id}")
  public Result detail(@PathVariable final Long id) {
    final AccountWithRoleDO account = this.accountService.getByIdWithRole(id);
    return ResultGenerator.genOkResult(account);
  }

  @ApiOperation(value = "获取账户列表")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "page", value = "页号", dataType = "Integer", example = "1"),
    @ApiImplicitParam(name = "size", value = "页大小", dataType = "Integer", example = "10")
  })
  @Cacheable(value = "account.list", unless = "#result == null or #result.code != 200")
  @CacheExpire(expire = 60)
  @GetMapping
  public Result list(
      @RequestParam(defaultValue = "0") final Integer page,
      @RequestParam(defaultValue = "0") final Integer size) {
    AccountController.log.debug("==> No cache, find database");
    PageHelper.startPage(page, size);
    final List<AccountDO> list = this.accountService.listAll();
    final PageInfo<AccountDO> pageInfo = PageInfo.of(list);
    // 不显示 password 字段
    final PageInfo<JSONObject> objectPageInfo =
        JsonUtils.deleteFields(pageInfo, PageInfo.class, "password");
    return ResultGenerator.genOkResult(objectPageInfo);
  }
}

package com.zoctan.seedling.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zoctan.seedling.core.cache.CacheExpire;
import com.zoctan.seedling.core.jwt.JwtUtil;
import com.zoctan.seedling.core.response.Result;
import com.zoctan.seedling.core.response.ResultGenerator;
import com.zoctan.seedling.model.Account;
import com.zoctan.seedling.service.AccountService;
import com.zoctan.seedling.service.impl.UserDetailsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.security.Principal;
import java.util.List;

/**
 * @author Zoctan
 * @date 2018/05/27
 */
@Api(value = "账户接口")
@RestController
@RequestMapping("/account")
@Validated
public class AccountController {
    private final static Logger log = LoggerFactory.getLogger(AccountController.class);
    @Resource
    private AccountService accountService;
    @Resource
    private UserDetailsServiceImpl userDetailsService;
    @Resource
    private JwtUtil jwtUtil;

    @ApiOperation(value = "账户注册", notes = "根据传过来的account信息来注册账户")
    @ApiImplicitParam(name = "account", value = "账户实体", required = true, dataType = "Account")
    @PostMapping
    public Result register(@Valid @RequestBody final Account account,
                           final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            final String msg = bindingResult.getFieldError().getDefaultMessage();
            return ResultGenerator.genFailedResult(msg);
        } else {
            // 保存后密码加密了，而登录需要没加密的密码
            final String password = account.getPassword();
            this.accountService.save(account);
            return this.login(account.getName(), password);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "删除账户", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(name = "id", value = "账户Id", required = true, dataType = "Long")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable final Long id) {
        this.accountService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @ApiOperation(value = "更新账户信息", notes = "根据传过来的account信息来更新账户详细信息")
    @ApiImplicitParam(name = "account", value = "账户实体", required = true, dataType = "Account")
    @PutMapping
    public Result update(@RequestBody final Account account) {
        this.accountService.update(account);
        return ResultGenerator.genOkResult();
    }

    @ApiOperation(value = "获取账户信息")
    @ApiImplicitParam(name = "id", value = "账户Id", required = true, dataType = "Long")
    @GetMapping("/{id}")
    public Result detail(@PathVariable final Long id) {
        final Account account = this.accountService.findById(id);
        // {"data":{"email":"admin@qq.com","id":1,"lastLoginTime":1517461200000,"registerTime":1514782800000,"roleList":[{"id":0,"name":"admin"},{"id":0,"name":"test"}],"name":"admin"},"message":"OK","status":200}
        return ResultGenerator.genOkResult(account);
    }

    @ApiOperation(value = "获取账户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页号", dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页数", dataType = "Integer")
    })
    @Cacheable(value = "account.list", unless = "#result == null or #result.code != 200")
    @CacheExpire(expire = 60)
    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") final Integer page,
                       @RequestParam(defaultValue = "0") final Integer size) {
        log.debug("no cache => find in database");
        PageHelper.startPage(page, size);
        final List<Account> list = this.accountService.findAll();
        final PageInfo<Account> pageInfo = new PageInfo<>(list);
        // {"data":{"endRow":2,"firstPage":0,"hasNextPage":false,"hasPreviousPage":false,"isFirstPage":false,"isLastPage":true,"lastPage":0,"list":[{"email":"admin@qq.com","id":1,"lastLoginTime":1517461200000,"registerTime":1514782800000,"roleList":null,"name":"admin"},{"email":"user@qq.com","id":2,"lastLoginTime":1517461200000,"registerTime":1514782800000,"roleList":null,"username":"user"}],"navigateFirstPage":0,"navigateLastPage":0,"navigatePages":8,"navigatepageNums":[],"nextPage":0,"orderBy":"","pageNum":0,"pageSize":0,"pages":0,"prePage":0,"size":2,"startRow":1,"total":2},"message":"OK","status":200}
        return ResultGenerator.genOkResult(pageInfo);
    }

    @ApiOperation(value = "账户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "账户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "账户密码", required = true, dataType = "String")
    })
    @PostMapping("/login")
    public Result login(@NotEmpty(message = "账户名不能为空")
                        @RequestParam("name") final String name,
                        @NotEmpty(message = "密码不能为空")
                        @RequestParam("password") final String password) {
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(name);
        if (this.accountService.verifyPassword(password, userDetails.getPassword())) {
            // 更新登录时间
            this.accountService.updateLoginTimeByName(name);
            return ResultGenerator.genOkResult(this.jwtUtil.sign(name, userDetails.getAuthorities()));
        } else {
            return ResultGenerator.genFailedResult("密码错误");
        }
    }

    /**
     * SpringSecurity注入Principal，可以获得当前账户
     */
    @ApiOperation(value = "账户注销")
    @GetMapping("/logout")
    public Result logout(final Principal account) {
        this.jwtUtil.invalidRedisToken(account.getName());
        return ResultGenerator.genOkResult();
    }
}

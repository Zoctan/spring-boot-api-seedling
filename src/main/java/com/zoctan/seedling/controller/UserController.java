package com.zoctan.seedling.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zoctan.seedling.core.jwt.JwtUtil;
import com.zoctan.seedling.core.response.Result;
import com.zoctan.seedling.core.response.ResultGenerator;
import com.zoctan.seedling.model.User;
import com.zoctan.seedling.service.UserService;
import com.zoctan.seedling.service.impl.UserDetailsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by Zoctan on 2018/02/04.
 */
@Api(value = "用户接口")
@RestController
@RequestMapping("/user")
@Validated
@Slf4j
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private UserDetailsServiceImpl userDetailsService;

    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(final JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @ApiOperation(value = "用户注册", notes = "根据传过来的user信息来注册用户")
    @ApiImplicitParam(name = "user", value = "用户实体", required = true, dataType = "User")
    @PostMapping
    public Result register(@Valid @RequestBody final User user,
                           final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            final String msg = bindingResult.getFieldError().getDefaultMessage();
            return ResultGenerator.genFailedResult(msg);
        } else {
            // 保存后密码加密了，而登录需要没加密的密码
            final String password = user.getPassword();
            this.userService.save(user);
            return this.login(user.getUsername(), password);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "删除用户", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(name = "id", value = "用户Id", required = true, dataType = "Long")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable final Long id) {
        this.userService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @ApiOperation(value = "更新用户信息", notes = "根据传过来的user信息来更新用户详细信息")
    @ApiImplicitParam(name = "user", value = "用户实体", required = true, dataType = "User")
    @PutMapping
    public Result update(@RequestBody final User user) {
        this.userService.update(user);
        return ResultGenerator.genOkResult();
    }

    @ApiOperation(value = "获取用户信息")
    @ApiImplicitParam(name = "id", value = "用户Id", required = true, dataType = "Long")
    @GetMapping("/{id}")
    public Result detail(@PathVariable final Long id) {
        final User user = this.userService.findById(id);
        // {"data":{"email":"admin@qq.com","id":1,"lastLoginTime":1517461200000,"registerTime":1514782800000,"roleList":[{"id":0,"name":"admin"},{"id":0,"name":"test"}],"username":"admin"},"message":"OK","status":200}
        return ResultGenerator.genOkResult(user);
    }

    @ApiOperation(value = "获取用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页号", dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页数", dataType = "Integer")
    })
    @Cacheable(value = "userList")
    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") final Integer page,
                       @RequestParam(defaultValue = "0") final Integer size) {
        log.info("no cache => find in database");
        PageHelper.startPage(page, size);
        final List<User> list = this.userService.findAll();
        final PageInfo pageInfo = new PageInfo(list);
        // {"data":{"endRow":2,"firstPage":0,"hasNextPage":false,"hasPreviousPage":false,"isFirstPage":false,"isLastPage":true,"lastPage":0,"list":[{"email":"admin@qq.com","id":1,"lastLoginTime":1517461200000,"registerTime":1514782800000,"roleList":null,"username":"admin"},{"email":"user@qq.com","id":2,"lastLoginTime":1517461200000,"registerTime":1514782800000,"roleList":null,"username":"user"}],"navigateFirstPage":0,"navigateLastPage":0,"navigatePages":8,"navigatepageNums":[],"nextPage":0,"orderBy":"","pageNum":0,"pageSize":0,"pages":0,"prePage":0,"size":2,"startRow":1,"total":2},"message":"OK","status":200}
        return ResultGenerator.genOkResult(pageInfo);
    }

    @ApiOperation(value = "用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "用户密码", required = true, dataType = "String")
    })
    @PostMapping("/login")
    public Result login(@NotEmpty(message = "用户名不能为空")
                        @RequestParam("username") final String username,
                        @NotEmpty(message = "密码不能为空")
                        @RequestParam("password") final String password) {
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        if (this.userService.verifyPassword(password, userDetails.getPassword())) {
            // 更新登录时间
            this.userService.updateLoginTime(username);
            return ResultGenerator.genOkResult(this.jwtUtil.sign(username, userDetails.getAuthorities()));
        } else {
            return ResultGenerator.genFailedResult("密码错误");
        }
    }

    /**
     * @AuthenticationPrincipal 该注解可以获得当前用户
     * https://docs.spring.io/spring-security/site/docs/3.2.3.RELEASE/reference/htmlsingle/#mvc-authentication-principal
     */
    @ApiOperation(value = "用户注销")
    @GetMapping("/logout")
    public Result logout(@AuthenticationPrincipal final UserDetails userDetails) {
        this.jwtUtil.invalidRedisStore(userDetails.getUsername());
        return ResultGenerator.genOkResult();
    }
}

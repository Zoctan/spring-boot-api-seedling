package com.zoctan.seedling.core.config;

import com.zoctan.seedling.core.filter.AuthenticationFilter;
import com.zoctan.seedling.core.filter.MyAuthenticationEntryPoint;
import com.zoctan.seedling.service.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * 安全设置
 *
 * @author Zoctan
 * @date 2018/05/27
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private MyAuthenticationEntryPoint myAuthenticationEntryPoint;
    @Resource
    private AuthenticationFilter authenticationFilter;

    @Bean
    @Override
    public UserDetailsServiceImpl userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    /**
     * 使用随机加盐哈希算法对密码进行加密
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 默认强度10，可以指定 4 到 31 之间的强度
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth)
            throws Exception {
        auth
                // 自定义获取账户信息
                .userDetailsService(this.userDetailsService())
                // 设置密码加密
                .passwordEncoder(this.passwordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity http)
            throws Exception {
        http    // 关闭 cors 验证
                .cors().disable()
                // 关闭 csrf 验证
                .csrf().disable()
                // 无状态 session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 异常处理
                .exceptionHandling()
                // 因为 RESTFul 没有登录界面所以只能显示未登录
                .authenticationEntryPoint(this.myAuthenticationEntryPoint)
                .and()
                // 对所有的请求都做权限校验
                .authorizeRequests()
                // 允许匿名请求
                .antMatchers(
                        "/swagger-ui.html**",
                        "/swagger-resources**",
                        "/webjars/**",
                        "/v2/**"
                ).permitAll()
                // 允许登录和注册
                .antMatchers(
                        HttpMethod.POST,
                        "/account/login",
                        "/account"
                ).permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        http    // 身份过滤器
                .addFilterBefore(this.authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // 禁用页面缓存
        http.headers().cacheControl();
    }
}
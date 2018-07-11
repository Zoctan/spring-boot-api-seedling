package com.zoctan.seedling.service.impl;

import com.zoctan.seedling.model.Account;
import com.zoctan.seedling.model.Role;
import com.zoctan.seedling.service.AccountService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zoctan
 * @date 2018/05/27
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(final String name) throws UsernameNotFoundException {
        final Account account = this.accountService.findByName(name);
        if (account == null) {
            throw new UsernameNotFoundException("name not existed");
        }
        final List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (final Role role : account.getRoleList()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new org.springframework.security.core.userdetails.User(
                account.getName(),
                account.getPassword(),
                authorities
        );
    }
}

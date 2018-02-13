package com.zoctan.fast.service.impl;

import com.zoctan.fast.model.Role;
import com.zoctan.fast.model.User;
import com.zoctan.fast.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zoctan on 2018/02/04.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = this.userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("username not existed");
        }
        final List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (final Role role : user.getRoleList()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}

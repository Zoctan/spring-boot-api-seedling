package com.zoctan.seedling.core.filter;

import com.zoctan.seedling.core.jwt.JwtUtil;
import com.zoctan.seedling.util.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 身份认证过滤器
 *
 * @author Zoctan
 * @date 2018/05/27
 */
@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    private final static Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);
    @Resource
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request, @NonNull final HttpServletResponse response, @NonNull final FilterChain filterChain)
            throws ServletException, IOException {
        final String token = this.jwtUtil.getTokenFromRequest(request);
        if (token != null) {
            final String name = this.jwtUtil.getName(token);
            log.debug("account<{}> token => {}", name, token);

            if (name != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (this.jwtUtil.validateToken(token)) {
                    final UsernamePasswordAuthenticationToken authentication = this.jwtUtil.getAuthentication(name, token);

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 向Security上下文中注入已认证的账户
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("account<{}> is authorized, set security context", name);
                }
            }
        } else {
            log.debug("Anonymous<{}> request URL<{}>", IpUtils.getIpAddress(request), request.getRequestURL());
        }
        filterChain.doFilter(request, response);
    }
}

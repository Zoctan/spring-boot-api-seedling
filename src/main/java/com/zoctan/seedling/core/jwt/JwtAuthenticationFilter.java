package com.zoctan.seedling.core.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @date 2018/5/27
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final static Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Resource
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
            throws ServletException, IOException {
        final String token = this.jwtUtil.getTokenFromRequest(request);
        if (token != null) {
            final String username = this.jwtUtil.getUsername(token);
            log.info("JwtFilter => user<{}> token : {}", username, token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (this.jwtUtil.validateToken(token)) {
                    final UsernamePasswordAuthenticationToken authentication = this.jwtUtil.getAuthentication(username, token);

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("JwtFilter => user<{}> is authorized, set security context", username);
                }
            }
        } else {
            log.info("Anonymous request URL<{}>", request.getRequestURL());
        }
        filterChain.doFilter(request, response);
    }
}

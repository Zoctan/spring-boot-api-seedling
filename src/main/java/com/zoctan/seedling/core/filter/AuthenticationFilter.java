package com.zoctan.seedling.core.filter;

import com.zoctan.seedling.core.jwt.JwtUtil;
import com.zoctan.seedling.util.IpUtils;
import com.zoctan.seedling.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.*;
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
@Order(2)
public class AuthenticationFilter implements Filter {
    private final static Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Resource
    private JwtUtil jwtUtil;

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        log.debug("AuthenticationFilter init");
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain)
            throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        final String token = this.jwtUtil.getTokenFromRequest(request);
        if (!StringUtils.isEmpty(token)) {
            final String name = this.jwtUtil.getName(token);
            log.debug("Account<{}> token => {}", name, token);

            if (name != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (this.jwtUtil.validateToken(token)) {
                    final UsernamePasswordAuthenticationToken authentication = this.jwtUtil.getAuthentication(name, token);

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 向 security 上下文中注入已认证的账户
                    // 之后可以直接在控制器 controller 的入参获得 Principal 或 Authentication
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("Account<{}> is authorized, set security context", name);
                }
            }
        } else {
            log.debug("Anonymous<{}> request URL<{}>", IpUtils.getIpAddress(request), UrlUtils.getMappingUrl(request));
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        log.debug("AuthenticationFilter destroy");
    }
}

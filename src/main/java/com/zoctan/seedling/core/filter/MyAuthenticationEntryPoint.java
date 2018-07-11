package com.zoctan.seedling.core.filter;

import com.zoctan.seedling.core.response.ResultGenerator;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * 认证入口点
 * 因为RESTFul没有登录界面所以只能显示未登录
 *
 * @author Zoctan
 * @date 2018/05/27
 */
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("Content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        response.getWriter().println(ResultGenerator.genUnauthorizedResult().toString());
        response.getWriter().close();
    }
}

package com.zoctan.seedling;

import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 账户接口测试
 *
 * @author Zoctan
 * @date 2018/05/27
 */
public class AccountControllerTest extends BaseControllerTest {

    @SuppressWarnings("FieldCanBeLocal")
    private final String resource = "/account";

    /**
     * MockMvc 测试 SpringSecurity
     * 可以参考官网 https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/
     * 带上JWT测试可以参考 StackOverFlow
     * https://stackoverflow.com/questions/29510759/how-to-test-spring-security-oauth2-resource-server-security
     */
    @Test(timeout = 5000)
    public void login() throws Exception {
        final String targetUrl = this.url + this.resource + "/login";
        this.mockMvc.perform(post(targetUrl)
                .param("name", "admin")
                .param("password", "admin"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }
}
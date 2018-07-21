package com.zoctan.seedling.core.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Json web token 配置
 *
 * @author Zoctan
 * @date 2018/06/09
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigurationProperties {
    /**
     * claim authorities key
     */
    private String claimKeyAuth;
    /**
     * token 前缀
     */
    private String tokenType;
    /**
     * 请求头或请求参数的key
     */
    private String header;
    /**
     * 有效期
     */
    private long expireTime;

    public String getHeader() {
        return this.header;
    }

    public void setHeader(final String header) {
        this.header = header;
    }

    public long getExpireTime() {
        return this.expireTime;
    }

    public void setExpireTime(final long expireTime) {
        this.expireTime = expireTime;
    }

    public String getClaimKeyAuth() {
        return this.claimKeyAuth;
    }

    public void setClaimKeyAuth(final String claimKeyAuth) {
        this.claimKeyAuth = claimKeyAuth;
    }

    public String getTokenType() {
        return this.tokenType;
    }

    public void setTokenType(final String tokenType) {
        this.tokenType = tokenType;
    }
}

package com.zoctan.seedling.core.rsa;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * RSA 配置
 *
 * @author Zoctan
 * @date 2018/07/20
 */
@Component
@ConfigurationProperties(prefix = "rsa")
public class RsaConfigurationProperties {
    /**
     * 私钥位置
     * 默认：classpath:resources/rsa/private-key.pem
     */
    private String privateKeyPath = "rsa/private-key.pem";
    /**
     * 公钥位置
     * 默认：classpath:resources/rsa/public-key.pem
     */
    private String publicKeyPath = "rsa/public-key.pem";

    public String getPrivateKeyPath() {
        return this.privateKeyPath;
    }

    public void setPrivateKeyPath(final String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    public String getPublicKeyPath() {
        return this.publicKeyPath;
    }

    public void setPublicKeyPath(final String publicKeyPath) {
        this.publicKeyPath = publicKeyPath;
    }
}

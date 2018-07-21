package com.zoctan.seedling.core.config;

import com.zoctan.seedling.core.rsa.RsaUtils;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Base64Utils;

import javax.annotation.Resource;

/**
 * Jasypt 配置
 *
 * @author Zoctan
 * @date 2018/07/21
 */
@Configuration
public class JasyptConfig {
    @Value("${jasypt.encryptor.password}")
    private String passwordEncryptedByBase64AndRSA;
    @Resource
    private RsaUtils rsaUtils;

    @Bean
    public StringEncryptor myStringEncryptor() throws Exception {
        // Base64 + RSA 加密的密码
        final byte[] passwordEncryptedByRSA = Base64Utils.decodeFromString(this.passwordEncryptedByBase64AndRSA);
        final String password = new String(this.rsaUtils.decrypt(passwordEncryptedByRSA));
        // 配置
        final PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        final SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password);
        // 加密算法
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }
}

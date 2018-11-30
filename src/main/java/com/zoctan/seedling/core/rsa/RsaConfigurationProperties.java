package com.zoctan.seedling.core.rsa;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * RSA 配置
 *
 * @author Zoctan
 * @date 2018/07/20
 */
@Data
@Component
@ConfigurationProperties(prefix = "rsa")
public class RsaConfigurationProperties {
  /** 私钥位置 默认：classpath:resources/rsa/private-key.pem */
  private String privateKeyPath = "rsa/private-key.pem";
  /** 公钥位置 默认：classpath:resources/rsa/public-key.pem */
  private String publicKeyPath = "rsa/public-key.pem";
}

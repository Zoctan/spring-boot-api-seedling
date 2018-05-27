package com.zoctan.seedling;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

import static com.zoctan.seedling.core.ProjectConstant.MAPPER_PACKAGE;

/**
 * @author Zoctan
 * @date 2018/5/27
 */
@EnableEncryptableProperties
@SpringBootApplication
@EnableCaching
@EnableTransactionManagement
@MapperScan(basePackages = MAPPER_PACKAGE)
public class Application {
    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

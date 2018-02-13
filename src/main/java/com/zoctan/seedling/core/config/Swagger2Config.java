package com.zoctan.seedling.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.zoctan.seedling.core.ProjectConstant.BASE_PACKAGE;

/**
 * Swagger2 在线API文档
 * http://springfox.github.io/springfox/docs/current/#getting-started
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket buildDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.buildApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo buildApiInfo() {
        final Contact contact = new Contact(
                "Zoctan",
                "Zoctan.github.io",
                "752481828@qq.com"
        );

        return new ApiInfoBuilder()
                .title("APIs doc")
                .description("RESTful APIs")
                .termsOfServiceUrl("https://github.com/Zoctan/spring-boot-api-fast")
                .contact(contact)
                .version("1.0")
                .license("Apache License 2.0")
                .licenseUrl("https://github.com/Zoctan/spring-boot-api-fast/blob/master/LICENSE")
                .build();
    }

}
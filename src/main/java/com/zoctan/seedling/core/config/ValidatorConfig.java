package com.zoctan.seedling.core.config;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * 参数校验
 * https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-constraint-violation-methods
 *
 * @author Zoctan
 * @date 2018/05/27
 */
@Configuration
public class ValidatorConfig {
//  @Bean
//  public MethodValidationPostProcessor methodValidationPostProcessor() {
//    // 默认是普通模式（校验完所有的属性，返回所有验证失败的异常）
//    // return new MethodValidationPostProcessor();
//    final MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
//    // 快速失败普通模式（只要有一个验证失败，则返回异常）
//    postProcessor.setValidator(this.validatorFailFast());
//    return postProcessor;
//  }

  @Bean
  public Validator validatorFailFast() {
    return Validation.byProvider(HibernateValidator.class)
        .configure()
        .failFast(true)
        .buildValidatorFactory()
        .getValidator();
  }
}

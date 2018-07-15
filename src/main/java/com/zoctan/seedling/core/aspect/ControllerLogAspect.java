package com.zoctan.seedling.core.aspect;

import com.zoctan.seedling.util.IpUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 控制器日志切面
 *
 * @author Zoctan
 * @date 2018/07/13
 */
@Aspect
@Component
public class ControllerLogAspect {
    private final static Logger log = LoggerFactory.getLogger(ControllerLogAspect.class);

    @Pointcut("execution(* com.zoctan.seedling.controller..*.*(..))")
    public void controllerLog() {
    }

    /**
     * 环绕通知,环绕增强，相当于 MethodInterceptor
     */
    @Around("controllerLog()")
    public Object around(final ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("[0] 方法环绕开始");
        return joinPoint.proceed();
    }

    @Before("controllerLog()")
    public void deBefore(final JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        final ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        final HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        log.debug("[1] URL => {}", request.getRequestURL());
        log.debug("[2] 请求方法 => {}", request.getMethod());
        log.debug("[3] IP => {}", IpUtils.getIpAddress(request));
        log.debug("[4] 类方法 => {}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.debug("[5] 参数 => {}", Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "result", pointcut = "controllerLog()")
    public void doAfterReturning(final Object result) {
        // 处理完请求，返回内容
        log.debug("[6] 方法的返回值 => {}", result);
    }

    /**
     * 后置异常通知
     */
    @AfterThrowing("controllerLog()")
    public void afterThrows(final JoinPoint joinPoint) {
        log.debug("[6] 方法异常");
    }
}
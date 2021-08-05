package com.adjs.gaoleng_plus.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.UUID;

@Component
@Aspect
public class WebLogAcpect {

    private static final Logger logger  = LoggerFactory.getLogger(WebLogAcpect.class);


    @Pointcut("execution(public * com.adjs.gaoleng_plus.controller..*.*(..))")
    public void webLog(){}

    public static ThreadLocal<String> threadLocalLog = new ThreadLocal<>();

    /**
     * 前置通知：在连接点之前执行的通知
     * @param joinPoint
     * @throws Throwable
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        threadLocalLog.set(uuid);
        String logInfo = String.format("URL: %s, HTTP_METHOD: %s, IP: %s, CLASS_METHOD: %s, ARGS: %s", request.getRequestURL().toString(), request.getMethod(), request.getRemoteAddr(),
                joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
        // 记录下请求内容
        logger.info(uuid + "| REQUEST --- " + logInfo);
    }

    @AfterReturning(returning = "ret",pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        String uuid = threadLocalLog.get();
        // 处理完请求，返回内容
        logger.info(uuid + "| RESPONSE --- " + ret);
    }
}

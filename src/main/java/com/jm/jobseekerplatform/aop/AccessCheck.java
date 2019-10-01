package com.jm.jobseekerplatform.aop;

import com.jm.jobseekerplatform.annotation.LoggedUserAccessCheck;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class AccessCheck {

    private static final Logger logger = LoggerFactory.getLogger(AccessCheck.class);

    @Pointcut("execution(public * com.jm.jobseekerplatform.controller.rest.SeekerProfileRestController.*(..))")
//    @Pointcut("@annotation(com.jm.jobseekerplatform.annotation.LoggedUserAccessCheck)")
    public void pointMethod() {
    }

    @Around("pointMethod()")
    public Object aroundCallMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.debug("Signature: {}", joinPoint.getSignature().toShortString());
        logger.debug("Static: {}", joinPoint.getStaticPart().toShortString());
        logger.debug("Target: {}", joinPoint.getTarget().toString());
        logger.debug("This: {}", joinPoint.getThis().toString());
        logger.debug("Kind: {}", joinPoint.getKind());
//        String args = Arrays.stream(joinPoint.getArgs())
//                .map(Object::toString)
//                .collect(Collectors.joining(","));
//        logger.debug("Before: {}, args={{}}", joinPoint.toShortString(), args);
        Class<?> clazz = joinPoint.getTarget().getClass();
        Method method = getMethod(clazz, joinPoint.getSignature().getName());
        logger.debug("Method: {}", method.toString());
        Annotation annotation = method.getAnnotation(LoggedUserAccessCheck.class);
        logger.debug("Method anno: {}", annotation);
        return joinPoint.proceed();
    }

//    @After("pointMethod()")
//    public void afterCallMethod(JoinPoint joinPoint) {
//        logger.debug("After: {}", joinPoint.toShortString());
//    }

    private Method getMethod(Class<?> clazz, String methodName) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.getName().equals(methodName))
                .findAny()
                .orElse(null);
    }

}

package com.jm.jobseekerplatform.annotation.processor;

import com.jm.jobseekerplatform.annotation.AccessCheck;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * {@link org.springframework.beans.factory.config.BeanPostProcessor} implementation for AccessCheck annotations
 */
public class AccessCheckAnnotationBeanPostProcessor implements BeanPostProcessor {

    private static final GrantedAuthority ADMIN_AUTHORITY = new SimpleGrantedAuthority("ROLE_ADMIN");
    private final Logger logger = LoggerFactory.getLogger(AccessCheckAnnotationBeanPostProcessor.class);
    private Map<String, Class> beanClassMap = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        logger.debug("Processing bean: {}", beanName);
        Class<?> beanClass = bean.getClass();
        for (Method method : ReflectionUtils.getAllDeclaredMethods(beanClass)) {
            if (method.isAnnotationPresent(AccessCheck.class)) {
                beanClassMap.put(beanName, beanClass);
                logger.debug("Before: {}  class: {}  interfaces: [{}]", beanName, bean.getClass().getSimpleName(),
                        Arrays.stream(ClassUtils.getAllInterfaces(bean))
                                .map(Class::getSimpleName)
                                .collect(Collectors.joining(", ")));
                break;
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = beanClassMap.get(beanName);
        if (beanClass != null) {
//            Object proxy = Proxy.newProxyInstance(bean.getClass().getClassLoader(), ClassUtils.getAllInterfaces(bean),
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(beanClass);
            enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
                Method origMethod = ClassUtils.getMethodIfAvailable(beanClass, method.getName(), method.getParameterTypes());
                if (origMethod != null) {
                    if (origMethod.isAnnotationPresent(AccessCheck.class)) {
                        logger.debug("Start proxy method: {}  bean: {}", method.getName(), beanName);
                        doAccessCheck(method, args);
                        Object retVal = method.invoke(bean, args);
                        logger.debug("Stop proxy method: {}  bean: {}", method.getName(), beanName);
                        return retVal;
                    }
                }
                return method.invoke(bean, args);
            });
            Object proxy = enhancer.create();
            logger.debug("After: {}  class: {}  bean: {}  interfaces: [{}]  proxy: {}  interfaces: [{}]", beanName, beanClass.getSimpleName(),
                    bean.getClass().getSimpleName(), Arrays.stream(ClassUtils.getAllInterfaces(bean))
                            .map(Class::getSimpleName)
                            .collect(Collectors.joining(", ")),
                    proxy.getClass().getSimpleName(), Arrays.stream(ClassUtils.getAllInterfaces(proxy))
                            .map(Class::getSimpleName)
                            .collect(Collectors.joining(", ")));
            logger.debug("Proxy methods: [{}]", Arrays.stream(proxy.getClass().getDeclaredMethods())
                    .map(Method::getName)
                    .collect(Collectors.joining(", ")));
            return proxy;
        } else {
            return bean;
        }
    }

    private void doAccessCheck(Method method, Object[] args) {
        User loggedUser = getLoggedUser();
        logger.debug("User: {}  profile id: {}", loggedUser, loggedUser != null ? loggedUser.getProfile().getId() : null);
        if (!isAdmin(loggedUser) && method.getParameterCount() > 0) {
            Field field;
            Parameter param = method.getParameters()[0];
            Object arg = args[0];
            if (loggedUser == null) {
                String msg = String.format("User should be logged in to access to entity %s",
                        arg.getClass().getSimpleName());
                logger.warn(msg);
                throw new AccessDeniedException(msg);
            }
            logger.debug("Method: {}", method.getName());
            logger.debug("First param: {}  type: {}", param.getName(), param.getType().getSimpleName());
            logger.debug("First arg: {}  value: {}", arg.getClass().getSimpleName(), arg.toString());
            field = ReflectionUtils.findField(arg.getClass(), null, Profile.class);
            logger.debug("Profile field: {}", field != null ? field.getType().getSimpleName() : null);
            if (arg instanceof User) {
                User accessedUser = (User) arg;
                if (!loggedUser.getId().equals(accessedUser.getId())) {
                    String msg = String.format("User %s with id %d access denied to user id %d",
                            loggedUser.getUsername(),
                            loggedUser.getId(),
                            accessedUser.getId());
                    logger.warn(msg);
                    throw new AccessDeniedException(msg);
                }
                logger.debug("User {} with id {} access allowed to user id {}",
                        loggedUser.getUsername(),
                        loggedUser.getId(),
                        accessedUser.getId());
            } else if (arg instanceof Profile) {
                Profile accessedProfile = (Profile) arg;
                if (!loggedUser.getProfile().getId().equals(accessedProfile.getId())) {
                    String msg = String.format("User %s with profile id %d access denied to profile id %d",
                            loggedUser.getUsername(),
                            loggedUser.getProfile().getId(),
                            accessedProfile.getId());
                    logger.warn(msg);
                    throw new AccessDeniedException(msg);
                }
                logger.debug("User {} with profile id {} access allowed to profile id {}",
                        loggedUser.getUsername(),
                        loggedUser.getProfile().getId(),
                        accessedProfile.getId());
            } else if ((field = ReflectionUtils.findField(arg.getClass(), null, Profile.class)) != null) {
                logger.debug("Found field: {}  type: {}", field.getName(), field.getType().getSimpleName());
            } else {
                String msg = String.format("User %s with profile id %d access denied to entity %s",
                        loggedUser.getUsername(),
                        loggedUser.getProfile().getId(),
                        arg.getClass().getSimpleName());
                logger.warn(msg);
                throw new AccessDeniedException(msg);
            }
        }
    }

    private User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return (User) authentication.getPrincipal();
        } else {
            return null;
        }
    }

    private boolean isAdmin(User user) {
        if (user != null) {
            return user.getAuthority().equals(ADMIN_AUTHORITY);
        } else {
            return false;
        }
    }

}

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                logger.trace("Before: {}  class: {}  interfaces: {}", beanName, bean.getClass().getSimpleName(),
                        Arrays.stream(ClassUtils.getAllInterfaces(bean))
                                .map(Class::getSimpleName)
                                .toArray());
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
                        logger.trace("Enter proxy method: {}  bean: {}  caller: {}", method.getName(), beanName,
                                Thread.currentThread().getStackTrace()[3].toString());
                        checkAccess(beanClass, method, args);
                        Object retVal = method.invoke(bean, args);
                        logger.trace("Exit proxy method: {}  bean: {}  caller: {}", method.getName(), beanName,
                                Thread.currentThread().getStackTrace()[3].toString());
                        return retVal;
                    }
                }
                return method.invoke(bean, args);
            });
            Object proxy = enhancer.create();
            logger.trace("After: {}  class: {}  bean: {}  interfaces: {}  proxy: {}  interfaces: {}", beanName,
                    beanClass.getSimpleName(),
                    bean.getClass().getSimpleName(),
                    Arrays.stream(ClassUtils.getAllInterfaces(bean)).map(Class::getSimpleName).toArray(),
                    proxy.getClass().getSimpleName(),
                    Arrays.stream(ClassUtils.getAllInterfaces(proxy)).map(Class::getSimpleName).toArray());
            logger.trace("Proxy methods: [{}]", Arrays.stream(proxy.getClass().getDeclaredMethods())
                    .map(Method::getName)
                    .collect(Collectors.joining(", ")));
            return proxy;
        } else {
            return bean;
        }
    }

    private void checkAccess(Class<?> beanClass, Method method, Object[] args) {
        User loggedUser = getLoggedUser();
        logger.trace("User: {}", loggedUser);
        if (!isAdmin(loggedUser) && args != null && args.length > 0) {
            Object arg = Stream.of(args)
                    .filter(a -> !ClassUtils.isPrimitiveOrWrapper(a.getClass()))
                    .findFirst()
                    .orElse(args[0]);
            if (loggedUser == null) {
                String msg = String.format("User should be logged in to access to entity %s",
                        arg.getClass().getSimpleName());
                logger.warn(msg);
                throw new AccessDeniedException(msg);
            }
            User userEntity;
            Profile profileEntity;
            if ((userEntity = getEntityByType(User.class, args)) != null) {
                if (!loggedUser.getId().equals(userEntity.getId())) {
                    String msg = String.format("User %s with user id %d access denied to user id %d",
                            loggedUser.getUsername(),
                            loggedUser.getId(),
                            userEntity.getId());
                    logger.warn(msg);
                    throw new AccessDeniedException(msg);
                }
                logger.debug("User {} with user id {} access allowed to user id {}",
                        loggedUser.getUsername(),
                        loggedUser.getId(),
                        userEntity.getId());
            } else if ((profileEntity = getEntityByType(Profile.class, args)) != null) {
                if (!loggedUser.getProfile().getId().equals(profileEntity.getId())) {
                    String msg = String.format("User %s with profile id %d access denied to profile id %d",
                            loggedUser.getUsername(),
                            loggedUser.getProfile().getId(),
                            profileEntity.getId());
                    logger.warn(msg);
                    throw new AccessDeniedException(msg);
                }
                logger.debug("User {} with profile id {} access allowed to profile id {}",
                        loggedUser.getUsername(),
                        loggedUser.getProfile().getId(),
                        profileEntity.getId());
//            } else if () {
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

    private <T> T getEntityByType(Class<T> type, Object[] args) {
        logger.trace("Looking for: {}", type.getSimpleName());
        T entity = null;
        for (Object arg : args) {
            if (!ClassUtils.isPrimitiveOrWrapper(arg.getClass())) {
                if (ClassUtils.isAssignableValue(type, arg)) {
                    entity = (T) arg;
                    logger.trace("Found ARG: {}  type: {}", arg.toString(), arg.getClass().getSimpleName());
                    break;
                } else {
                    logger.trace("Arg: {}  fields: {}", arg.getClass().getSimpleName(),
                            Arrays.stream(arg.getClass().getDeclaredFields())
                                    .map(f -> f.getType().getSimpleName()) // + "(" + f.getType().getSuperclass().getSimpleName() + ")")
                                    .toArray());
//                    Field field = ReflectionUtils.findField(arg.getClass(), null, type);
                    Field field = getFieldByType(type, arg);
                    if (field != null) {
                        field.setAccessible(true);
                        entity = (T) ReflectionUtils.getField(field, arg);
                        logger.trace("Found field {}: {}  type: {}", field.getName(), entity, field.getType().getSimpleName());
                        break;
                    }
                }
            }
        }
        return entity;
    }

    private Field getFieldByType(Class<?> type, Object obj) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (ClassUtils.isAssignable(type, field.getType())) {
                return field;
            }
        }
        return null;
    }

    private User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            return principal instanceof User ? (User) principal : null;
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

package com.jm.jobseekerplatform.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация проверяет все аргументы метода на наличие сущности, унаследованной от User, или сущности, содержащей поле,
 * унаследованного от User, затем проверяет id найденной сущности или поля с id текущего залогиненного пользователя,
 * если id не совпадают, то выбрасывает исключение AccessDeniedException, если такая сущность или поле не найдены,
 * то проделывает все тоже самое для Profile, если ничего не найдено, то выбрасывает исключение AccessDeniedException.
 *
 * Для пользователя с ролью админ эти проверки не проводятся.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessCheck {

    String value() default "";

}

package com.example.authmoduls.common.utils;

import com.example.authmoduls.common.enums.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) // on Method level
public @interface Access {

    Role[] levels() default Role.ADMIN;
    String createdBy() default "AUTH";

}

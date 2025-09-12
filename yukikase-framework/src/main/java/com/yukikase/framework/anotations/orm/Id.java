package com.yukikase.framework.anotations.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {
    String name() default "";
    int length() default 255; // for VARCHAR
    boolean autoIncrement() default true;
}

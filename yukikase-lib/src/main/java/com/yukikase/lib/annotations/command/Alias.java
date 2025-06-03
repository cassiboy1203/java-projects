package com.yukikase.lib.annotations.command;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Aliases.class)
public @interface Alias {
    @NonNull String alias() default "";

    @NonNull String subcommand() default "";

    /**
     * Name of the variable of type {@link com.yukikase.lib.permission.Permission}
     */
    @NonNull String permission() default "";
}
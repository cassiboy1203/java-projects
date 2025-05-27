package com.yukikase.lib.annotations.command;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Aliases.class)
public @interface Alias {
    String alias() default "";

    String subcommand() default "";
}
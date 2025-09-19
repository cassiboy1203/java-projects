package com.yukikase.lib.annotations.command;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(Aliases.class)
public @interface Alias {
    String alias() default "";

    String[] subcommand() default {};

    String permission() default "";
}

package com.yukikase.lib.annotations.command;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.yukikase.lib.command.DummySuggestionProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Argument {
    String value();

    boolean optional() default false;

    int intMin() default Integer.MIN_VALUE;

    int intMax() default Integer.MAX_VALUE;

    long longMin() default Long.MIN_VALUE;

    long longMax() default Long.MAX_VALUE;

    float floatMin() default Float.MIN_VALUE;

    float floatMax() default Float.MAX_VALUE;

    double doubleMin() default Double.MIN_VALUE;

    double doubleMax() default Double.MAX_VALUE;

    String[] suggestions() default {};

    Class<? extends SuggestionProvider<CommandSourceStack>> completer() default DummySuggestionProvider.class;

    StringType stringType() default StringType.STRING;

    static enum StringType {
        WORD,
        STRING,
        GREEDY
    }
}

package com.yukikase.diframework.testclasses;

import com.yukikase.diframework.anotations.Component;

@Component
public class ClassWithNonDefaultConstructorWithoutInject {
    public final Class1 class1;

    public ClassWithNonDefaultConstructorWithoutInject(Class1 class1) {
        this.class1 = class1;
    }
}

package com.yukikase.framework.injection.testclasses;

import com.yukikase.framework.anotations.injection.Component;

@Component
public class ClassWithNonDefaultConstructorWithoutInject {
    public final Class1 class1;

    public ClassWithNonDefaultConstructorWithoutInject(Class1 class1) {
        this.class1 = class1;
    }
}

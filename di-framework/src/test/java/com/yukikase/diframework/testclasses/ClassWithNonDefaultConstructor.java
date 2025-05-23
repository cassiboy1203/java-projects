package com.yukikase.diframework.testclasses;

import com.yukikase.diframework.anotations.Component;
import com.yukikase.diframework.anotations.Inject;

@Component
public class ClassWithNonDefaultConstructor {
    public final Class1 class1;

    @Inject
    public ClassWithNonDefaultConstructor(Class1 class1) {
        this.class1 = class1;
    }
}

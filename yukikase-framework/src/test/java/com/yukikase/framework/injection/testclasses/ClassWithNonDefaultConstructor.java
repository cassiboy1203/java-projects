package com.yukikase.framework.injection.testclasses;

import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Inject;

@Component
public class ClassWithNonDefaultConstructor {
    public final Class1 class1;

    @Inject
    public ClassWithNonDefaultConstructor(Class1 class1) {
        this.class1 = class1;
    }
}

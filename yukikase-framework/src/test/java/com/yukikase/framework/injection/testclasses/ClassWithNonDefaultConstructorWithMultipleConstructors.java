package com.yukikase.framework.injection.testclasses;

import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Inject;

@Component
public class ClassWithNonDefaultConstructorWithMultipleConstructors {
    public final Class1 class1;
    public final Class2 class2;

    public ClassWithNonDefaultConstructorWithMultipleConstructors(Class1 class1) {
        this.class1 = class1;
        this.class2 = null;
    }

    @Inject
    public ClassWithNonDefaultConstructorWithMultipleConstructors(Class2 class2) {
        this.class1 = null;
        this.class2 = class2;
    }
}

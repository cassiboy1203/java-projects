package com.yukikase.diframework.testclasses;

import com.yukikase.diframework.anotations.Component;
import com.yukikase.diframework.anotations.Inject;

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

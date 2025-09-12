package com.yukikase.framework.injection.testclasses;

import com.yukikase.framework.anotations.injection.Component;

@Component
public class ClassWithMethodsWithoutInject {
    public Class1 class1;
    public Class2 class2;
    public Class3 class3;

    public void setClass1(Class1 class1) {
        this.class1 = class1;
    }

    public void setClass2(Class2 class2) {
        this.class2 = class2;
    }

    public void setClass3(Class3 class3) {
        this.class3 = class3;
    }
}

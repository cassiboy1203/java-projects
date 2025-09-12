package com.yukikase.framework.injection.testclasses;

import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Inject;

import java.util.List;

@Component
public class ClassWithMethods {
    public Class1 class1;
    public Class2 class2;
    public Class3 class3;
    public Class7 class7;
    public List<SuperClassWithMultiple> superClassWithMultiples;
    public SuperClassWithMultiple[] superClassWithMultiplesArray;

    @Inject
    public void setClass1(Class1 class1) {
        this.class1 = class1;
    }

    @Inject
    public void setClass2(Class2 class2) {
        this.class2 = class2;
    }

    @Inject
    public void setSuperClassWithMultiples(List<SuperClassWithMultiple> superClassWithMultiples) {
        this.superClassWithMultiples = superClassWithMultiples;
    }

    @Inject
    public void setSuperClassWithMultiplesArray(SuperClassWithMultiple[] superClassWithMultiplesArray) {
        this.superClassWithMultiplesArray = superClassWithMultiplesArray;
    }
    
    //TODO: allow named parameters
//    @Inject
//    public void setClass7(@Qualifier("Class7") SuperClassWithMultiple class7) {
//        this.class7 = class7;
//    }

    public void setClass3(Class3 class3) {
        this.class3 = class3;
    }
}

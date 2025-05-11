package com.yukikase.diframework;

public class DIFramework {
    public Injector start(Class<?> mainClass) {
        var injector = new Injector();
        injector.start(mainClass);

        return injector;
    }
}

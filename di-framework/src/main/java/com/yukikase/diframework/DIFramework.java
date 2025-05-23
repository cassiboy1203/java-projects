package com.yukikase.diframework;


public class DIFramework {

    public static DefaultInjector start(Class<?>... mainClasses) {
        var injector = new DefaultInjector();
        injector.start(mainClasses);

        return injector;
    }
}

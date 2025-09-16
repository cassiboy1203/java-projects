package com.yukikase.framework.injection.testclasses;

import com.yukikase.framework.anotations.injection.Configuration;
import com.yukikase.framework.anotations.injection.Register;

@Configuration
public class ConfigurationClass {
    public static boolean register;
    public static boolean notRegister;

    @Register
    public void register() {
        register = true;
    }

    public void notRegister() {
        notRegister = true;
    }
}

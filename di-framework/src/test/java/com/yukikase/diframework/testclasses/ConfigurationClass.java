package com.yukikase.diframework.testclasses;

import com.yukikase.diframework.anotations.Configuration;
import com.yukikase.diframework.anotations.Register;

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

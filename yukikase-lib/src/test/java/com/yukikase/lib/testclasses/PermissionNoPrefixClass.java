package com.yukikase.lib.testclasses;

import com.yukikase.lib.PermissionHandler;
import com.yukikase.lib.annotations.Permission;

@Permission(value = "class", handler = PermissionHandler.IGNORE_PREFIX)
public class PermissionNoPrefixClass {
}

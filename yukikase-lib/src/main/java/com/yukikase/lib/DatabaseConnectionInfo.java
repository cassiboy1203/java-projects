package com.yukikase.lib;

import com.yukikase.framework.orm.DatabaseType;

public record DatabaseConnectionInfo(DatabaseType type, String host, String port, String user,
                                     String password) {

}

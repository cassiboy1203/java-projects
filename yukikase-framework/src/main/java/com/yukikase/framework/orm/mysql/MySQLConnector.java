package com.yukikase.framework.orm.mysql;

import com.yukikase.framework.anotations.injection.Singleton;
import com.yukikase.framework.orm.DatabaseConnector;

import java.sql.SQLException;

@Singleton
public class MySQLConnector extends DatabaseConnector {

    static {
        MAX_POOL_SIZE = 10;
    }

    public MySQLConnector(String host, String port, String database, String user, String password) throws SQLException {
        super("mysql", host, port, database, user, password);
    }
}

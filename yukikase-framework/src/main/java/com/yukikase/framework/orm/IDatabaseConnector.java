package com.yukikase.framework.orm;

import com.yukikase.framework.orm.mysql.MySQLConnector;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDatabaseConnector {
    static IDatabaseConnector create(DatabaseType type, String host, String port, String database, String user, String password) throws SQLException {
        return switch (type) {
            case MYSQL -> new MySQLConnector(host, port, database, user, password);
        };
    }

    Connection getConnection() throws SQLException;

    void shutdown() throws SQLException;
}

package com.yukikase.framework.orm;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;

import java.sql.SQLException;

public interface IDatabaseConnector {
    JdbcPooledConnectionSource getConnection() throws SQLException;

    void shutdown() throws Exception;
}

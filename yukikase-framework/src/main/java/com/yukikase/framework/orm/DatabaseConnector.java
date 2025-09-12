package com.yukikase.framework.orm;

import com.yukikase.framework.orm.proxy.ConnectionProxy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class DatabaseConnector implements IDatabaseConnector {
    protected final String connectionString;
    private final String username;
    private final String password;

    private final BlockingQueue<Connection> pool;

    protected static int MAX_POOL_SIZE = 0;

    protected DatabaseConnector(String driver, String host, String port, String database, String user, String password) throws SQLException {
        this.connectionString = "jdbc:" + driver + "://" + host + ":" + port + "/" + database;
        this.username = user;
        this.password = password;

        pool = new LinkedBlockingQueue<>(MAX_POOL_SIZE);

        var startupConnectionString = "jdbc:" + driver + "://" + host + ":" + port + "/";
        try (var conn = DriverManager.getConnection(startupConnectionString, username, password)) {
            var statement = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS `" + database + "`;");
            statement.execute();
        }

        for (int i = 0; i < MAX_POOL_SIZE; i++) {
            var conn = createConnection();
            pool.offer(conn);
        }
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(connectionString, username, password);
    }

    @Override
    public final Connection getConnection() throws SQLException {
        var conn = pool.poll();
        if (conn == null || conn.isClosed() || !conn.isValid(2)) {
            conn = createConnection();
        }
        return ConnectionProxy.wrap(conn, this);
    }


    public final void returnConnection(Connection conn) throws SQLException {
        if (conn != null && !conn.isClosed() && pool.size() < MAX_POOL_SIZE) {
            pool.offer(conn);
        }
    }

    @Override
    public final void shutdown() throws SQLException {
        for (var connection : pool) {
            connection.close();
        }
    }

}

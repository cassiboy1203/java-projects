package com.yukikase.framework.orm;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.framework.anotations.injection.Singleton;

import java.sql.DriverManager;
import java.sql.SQLException;

@Component
@Singleton
public class DatabaseConnector implements IDatabaseConnector {

    private final DatabaseInfo databaseInfo;

    private JdbcPooledConnectionSource connectionSource;

    @Inject
    public DatabaseConnector(DatabaseInfo databaseInfo) {
        this.databaseInfo = databaseInfo;
    }

    @Override
    public final JdbcPooledConnectionSource getConnection() throws SQLException {
        if (connectionSource == null) {
            var baseConnectionString = "jdbc:" + databaseInfo.getDatabaseType() + "://" + databaseInfo.getHost() + ":" + databaseInfo.getPort() + "/";
            try (var conn = DriverManager.getConnection(baseConnectionString, databaseInfo.getUser(), databaseInfo.getPassword());) {
                var stmt = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS `" + databaseInfo.getDatabaseType() + "`");
                stmt.executeUpdate();
            }
            connectionSource = new JdbcPooledConnectionSource(baseConnectionString + databaseInfo.getDatabase(), databaseInfo.getUser(), databaseInfo.getPassword());
            connectionSource.setMaxConnectionsFree(databaseInfo.getPoolSize());
        }

        return connectionSource;
    }

    @Override
    public final void shutdown() throws Exception {
        connectionSource.close();
    }

}

package com.yukikase.framework.orm;

import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Value;

@Component
public class DatabaseInfo {
    @Value("database.pool.size")
    private int poolSize;

    @Value("database.type")
    private String databaseType;

    @Value("database.host")
    private String host;

    @Value("database.port")
    private String port;

    @Value("database.database")
    private String database;

    @Value("database.user")
    private String user;

    @Value("database.password")
    private String password;

    public int getPoolSize() {
        if (poolSize <= 0) {
            return 5;
        }
        return poolSize;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}

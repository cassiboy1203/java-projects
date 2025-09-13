package com.yukikase.framework.orm.entity;

import com.yukikase.framework.injection.Injector;
import com.yukikase.framework.orm.DatabaseType;
import com.yukikase.framework.orm.IDatabaseConnector;
import com.yukikase.framework.orm.mysql.MySQLEntityFactory;

public interface EntityFactory {
    <T> EntitySet<T> get(Class<T> entityClass);

    static EntityFactory getInstance(DatabaseType databaseType, Injector injector) {
        return switch (databaseType) {
            case MYSQL ->
                    new MySQLEntityFactory(injector.getInstance(IDatabaseConnector.class), injector.getInstance(EntityTracker.class));
            default -> throw new IllegalStateException("Unsupported database type: " + databaseType);
        };
    }
}

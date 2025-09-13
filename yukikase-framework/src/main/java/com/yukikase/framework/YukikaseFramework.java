package com.yukikase.framework;


import com.yukikase.framework.exceptions.DatabaseStartupException;
import com.yukikase.framework.injection.DefaultInjector;
import com.yukikase.framework.injection.Injector;
import com.yukikase.framework.orm.DatabaseType;
import com.yukikase.framework.orm.IDatabaseConnector;
import com.yukikase.framework.orm.ITableGenerator;
import com.yukikase.framework.orm.OrmExtension;

import java.sql.SQLException;

public class YukikaseFramework {

    private static OrmExtension ormExtension;

    public static Injector start(Class<?>... mainClasses) {
        return start(false, mainClasses);
    }

    public static Injector start(boolean withDatabase, Class<?>... mainClasses) {
        var injector = new DefaultInjector();
        if (withDatabase) {
            ormExtension = new OrmExtension();
            injector.registerExtension(ormExtension);
        }
        injector.start(mainClasses);

        return injector;
    }

    public static IDatabaseConnector startDatabase(DatabaseType type, String host, String port, String database, String user, String password) {
        if (ormExtension == null) {
            return null;
        }
        IDatabaseConnector connector = null;
        try {
            connector = IDatabaseConnector.create(type, host, port, database, user, password);

            var tableGenerator = ITableGenerator.getInstance(type, connector);

            var sql = tableGenerator.updateTables(ormExtension.getEntities());
            sql = sql.trim();
            if (!sql.isEmpty()) {
                try (var conn = connector.getConnection()) {
                    System.out.println(sql);
                    conn.prepareStatement(sql).execute();
                }
            }

        } catch (SQLException e) {
            throw new DatabaseStartupException(e);
        }

        return connector;
    }
}

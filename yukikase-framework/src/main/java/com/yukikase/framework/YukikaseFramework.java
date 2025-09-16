package com.yukikase.framework;


import com.j256.ormlite.table.TableUtils;
import com.yukikase.framework.injection.DefaultInjector;
import com.yukikase.framework.injection.Injector;
import com.yukikase.framework.orm.IDatabaseConnector;
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

    public static void startDatabase(Injector injector) throws SQLException {
        if (ormExtension != null) {
            var conn = injector.getInstance(IDatabaseConnector.class).getConnection();
            for (var entity : ormExtension.getEntities()) {
                TableUtils.createTableIfNotExists(conn, entity);
            }
        }
    }
}

package com.yukikase.lib;

import com.yukikase.framework.YukikaseFramework;
import com.yukikase.framework.anotations.injection.Singleton;
import com.yukikase.framework.injection.Injector;
import com.yukikase.framework.orm.DatabaseType;
import com.yukikase.framework.orm.IDatabaseConnector;
import com.yukikase.framework.orm.entity.EntityFactory;
import org.bukkit.plugin.java.JavaPlugin;

@Singleton
public class YukikaseLib extends JavaPlugin {

    private DatabaseConnectionInfo databaseConnectionInfo;

    @Override
    public void onEnable() {
        saveDefaultConfig();
    }

    public Injector registerPlugin(YukikasePlugin plugin) {
        Injector injector;
        if (plugin.databaseEnabled()) {
            injector = YukikaseFramework.start(true, plugin.getClass(), YukikaseLib.class, YukikaseFramework.class);
        } else {
            injector = YukikaseFramework.start(plugin.getClass(), YukikaseLib.class);
        }
        injector.registerSingleton(YukikasePlugin.class, plugin);
        injector.registerSingleton(JavaPlugin.class, plugin);
        injector.registerSingleton(YukikaseLib.class, this);
        if (plugin.databaseEnabled()) {
            var info = getConnectionInfo();
            var databaseConnector = YukikaseFramework.startDatabase(info.type(), info.host(), info.port(), plugin.getName(), info.user(), info.password());
            injector.registerSingleton(IDatabaseConnector.class, databaseConnector);
            var entityFactory = EntityFactory.getInstance(info.type(), injector);
            injector.registerSingleton(EntityFactory.class, entityFactory);
        }
        injector.runConfigurations();
        return injector;
    }

    public DatabaseConnectionInfo getConnectionInfo() {
        if (databaseConnectionInfo == null) {
            var config = getConfig();
            databaseConnectionInfo = new DatabaseConnectionInfo(DatabaseType.valueOf(config.getString("database.type")), config.getString("database.host"), config.getString("database.port"), config.getString("database.user"), config.getString("database.password"));
        }

        return databaseConnectionInfo;
    }
}

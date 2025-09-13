package com.yukikase.framework.orm.mysql;

import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.framework.anotations.injection.Singleton;
import com.yukikase.framework.orm.IDatabaseConnector;
import com.yukikase.framework.orm.entity.EntityFactory;
import com.yukikase.framework.orm.entity.EntitySet;
import com.yukikase.framework.orm.entity.EntityTracker;

@Singleton
public class MySQLEntityFactory implements EntityFactory {
    private final IDatabaseConnector connector;
    private final EntityTracker tracker;

    @Inject
    public MySQLEntityFactory(IDatabaseConnector connector, EntityTracker tracker) {
        this.connector = connector;
        this.tracker = tracker;
    }

    @Override
    public <T> EntitySet<T> get(Class<T> entityClass) {
        return new MySQLEntitySet<>(connector, tracker, entityClass, this);
    }
}

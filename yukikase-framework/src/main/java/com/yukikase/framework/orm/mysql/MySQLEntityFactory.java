package com.yukikase.framework.orm.mysql;

import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.framework.anotations.orm.Column;
import com.yukikase.framework.anotations.orm.Entity;
import com.yukikase.framework.anotations.orm.Id;
import com.yukikase.framework.orm.IDatabaseConnector;
import com.yukikase.framework.orm.entity.EntityFactory;
import com.yukikase.framework.orm.entity.EntitySet;
import com.yukikase.framework.orm.entity.EntityTracker;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Component
public class MySQLEntityFactory implements EntityFactory {
    private final IDatabaseConnector connector;
    private final EntityTracker tracker;

    @Inject
    public MySQLEntityFactory(IDatabaseConnector connector, EntityTracker tracker) {
        this.connector = connector;
        this.tracker = tracker;
    }

    @Override
    public <T> void save(T entity) throws SQLException {
        Class<?> entityClass = entity.getClass();
        if (!entityClass.isAnnotationPresent(Entity.class)) {
            throw new IllegalArgumentException(entityClass.getName() + " is not an entity.");
        }

        var tableName = entityClass.getAnnotation(Entity.class).value().isEmpty() ? entityClass.getSimpleName() : entityClass.getAnnotation(Entity.class).value();

        var sjColumns = new StringJoiner(", ");
        var sjValues = new StringJoiner(", ");
        List<Object> values = new ArrayList<>();

        for (var field : entityClass.getDeclaredFields()) {
            var columnName = field.getName();
            if (field.isAnnotationPresent(Id.class)) {
                columnName = field.getAnnotation(Id.class).name().isEmpty() ? columnName : field.getAnnotation(Id.class).name();
            } else if (field.isAnnotationPresent(Column.class)) {
                columnName = field.getAnnotation(Column.class).name().isEmpty() ? columnName : field.getAnnotation(Column.class).name();
            }

            field.setAccessible(true);
            sjColumns.add(columnName);
            sjValues.add("?");
            try {
                values.add(field.get(entity));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            field.setAccessible(false);
        }

        var sqlString = "INSERT INTO " + tableName + " (" + sjColumns + ") VALUES (" + sjValues + ")";

        try (var conn = connector.getConnection()) {
            var stmt = conn.prepareStatement(sqlString);
            for (int i = 1; i <= values.size(); i++) {
                if (values.get(i - 1) instanceof Enum<?> value) {
                    stmt.setObject(i - 1, value.name());
                } else {
                    stmt.setObject(i, values.get(i - 1));
                }
            }
            stmt.executeUpdate();
        }
    }

    @Override
    public <T> EntitySet<T> get(Class<T> entityClass) {
        return new MySQLEntitySet<>(connector, tracker, entityClass);
    }
}

package com.yukikase.framework.orm.mysql;

import com.yukikase.framework.anotations.orm.Column;
import com.yukikase.framework.anotations.orm.Entity;
import com.yukikase.framework.anotations.orm.Id;
import com.yukikase.framework.exceptions.EntityCreationException;
import com.yukikase.framework.exceptions.NoEntityFoundException;
import com.yukikase.framework.orm.IDatabaseConnector;
import com.yukikase.framework.orm.entity.EntityFactory;
import com.yukikase.framework.orm.entity.EntitySet;
import com.yukikase.framework.orm.entity.EntityTracker;
import com.yukikase.framework.orm.query.Query;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public class MySQLEntitySet<T> implements EntitySet<T> {

    private static final Logger LOGGER = Logger.getLogger(MySQLEntitySet.class.getName());

    public Query getWhereQuery() {
        return whereQuery;
    }

    private Query whereQuery;
    private final List<OrderClause> orderClauses = new ArrayList<>();
    private int limit = -1;
    private int offset = -1;

    private final IDatabaseConnector connector;
    private final EntityTracker tracker;
    private final Class<T> entityClass;
    private final EntityFactory entityFactory;

    public MySQLEntitySet(IDatabaseConnector connector, EntityTracker tracker, Class<T> entityClass, EntityFactory entityFactory) {
        if (!entityClass.isAnnotationPresent(Entity.class)) {
            throw new IllegalArgumentException(entityClass.getName() + " is not an entity");
        }

        this.entityClass = entityClass;
        this.connector = connector;
        this.tracker = tracker;
        this.entityFactory = entityFactory;
    }

    @Override
    public EntitySet<T> where(Query query) {
        this.whereQuery = query;
        return this;
    }

    @Override
    public EntitySet<T> orderBy(String field) {
        return orderBy(field, true);
    }

    @Override
    public EntitySet<T> orderBy(String field, boolean descending) {
        orderClauses.add(new OrderClause(field, descending));
        return this;
    }

    @Override
    public EntitySet<T> limit(int limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public EntitySet<T> offset(int offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public T get(Object primaryKey) {
        var sb = new StringBuilder();
        sb.append("SELECT * FROM ").append(getTableName()).append(" WHERE ").append(getPrimaryKeyName()).append(" = ? ").append(" LIMIT 1");

        try (var conn = connector.getConnection()) {
            var statement = conn.prepareStatement(sb.toString());
            statement.setObject(1, primaryKey);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapToEntity(resultSet);
            }

            return null;
        } catch (SQLException e) {
            throw new NoEntityFoundException("Can not get entity from database");
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException |
                 NoSuchMethodException e) {
            throw new EntityCreationException("Can not create entity");
        }
    }

    @Override
    public Set<T> getAll() {
        var sb = new StringBuilder();
        sb.append("SELECT * FROM ").append(getTableName()).append(getWhere()).append(getOrderBy());
        if (limit > 0) {
            sb.append(" LIMIT ").append(limit);
        }
        if (offset > 0) {
            sb.append(" OFFSET ").append(offset);
        }
        var set = new HashSet<T>();
        try (var conn = connector.getConnection()) {
            var statement = conn.prepareStatement(sb.toString());
            var whereParams = whereQuery.params();
            for (int i = 1; i <= whereParams.size(); i++) {
                statement.setObject(i, whereParams.get(i - 1));
            }
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                set.add(mapToEntity(resultSet));
            }

        } catch (SQLException e) {
            throw new NoEntityFoundException("Can not get entity from database");
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            throw new EntityCreationException("Can not create entity");
        }

        return set;
    }

    @Override
    public Set<T> getAllNext() {
        var output = getAll();
        offset += limit;
        return output;
    }

    @Override
    public T next() {
        limit = 1;
        var outputs = getAll();
        if (offset < 0) {
            offset = limit;
        } else {
            offset += limit;
        }

        return outputs.stream().findFirst().orElse(null);
    }

    @Override
    public int count() {
        var sb = new StringBuilder();
        sb.append("SELECT COUNT(*) FROM ").append(getTableName()).append(getWhere()).append(getOrderBy());

        try (var conn = connector.getConnection()) {
            var statement = conn.prepareStatement(sb.toString());
            var whereParams = whereQuery.params();
            for (int i = 1; i <= whereParams.size(); i++) {
                statement.setObject(i, whereParams.get(i - 1));
            }

            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            return 0;
        }
        return 0;
    }

    @Override
    public void update(T entity) {
        var sb = new StringBuilder();
        sb.append("UPDATE ").append(getTableName()).append(" SET ");
        var params = new ArrayList<>();
        Object primaryKey = null;
        var sj = new StringJoiner(", ");
        var changedFields = tracker.getChangedFields(entity);
        for (var field : entityClass.getDeclaredFields()) {
            if (field.getType().isAnnotationPresent(Entity.class)) {
                var methodName = field.getName();
                Method method;
                try {
                    method = entityClass.getDeclaredMethod(methodName);
                } catch (NoSuchMethodException e) {
                    try {
                        method = entityClass.getDeclaredMethod("get" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1));
                    } catch (NoSuchMethodException ex) {
                        LOGGER.severe("No getter found for field: " + field.getName() + "(Valid names: get[FieldName] or [fieldName]). Field can not be saved");
                        continue;
                    }
                }

                try {
                    method.setAccessible(true);
                    var foreignEntity = method.invoke(entity);

                    if (foreignEntity != null) {
                        var entrySet = entityFactory.get(foreignEntity.getClass());
                        entrySet.update(foreignEntity, foreignEntity.getClass());
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                } finally {
                    method.setAccessible(false);
                }
            } else if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                try {
                    primaryKey = field.get(entity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } finally {
                    field.setAccessible(false);
                }
                continue;
            } else if (!changedFields.isEmpty() && !changedFields.contains(field.getName())) {
                continue;
            }
            var fieldName = field.getAnnotation(Column.class).name().isEmpty() ? field.getName() : field.getAnnotation(Column.class).name();
            sj.add(fieldName + " = ?");
            field.setAccessible(true);
            try {
                params.add(field.get(entity));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            field.setAccessible(false);
        }

        if (primaryKey == null) {
            throw new NoEntityFoundException("Primary key is null");
        }

        sb.append(sj);
        sb.append(" WHERE ").append(getPrimaryKeyName()).append(" = ? ");

        try (var conn = connector.getConnection()) {
            var statement = conn.prepareStatement(sb.toString());

            for (int i = 1; i <= params.size(); i++) {
                if (params.get(i - 1) instanceof Enum<?> value) {
                    statement.setObject(i - 1, value.name());
                } else {
                    statement.setObject(i, params.get(i - 1));
                }
            }
            statement.setObject(params.size() + 1, primaryKey);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new NoEntityFoundException("Can not get entity from database");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void update(Object entity, Class<?> entityClass) {
        if (this.entityClass == entityClass) {
            update((T) entity);
        }
    }

    @Override
    public void delete(Object primaryKey) {
        var sb = new StringBuilder();
        sb.append("DELETE FROM ").append(getTableName()).append("WHERE ").append(getPrimaryKeyName()).append(" = ? ");

        try (var conn = connector.getConnection()) {
            var statement = conn.prepareStatement(sb.toString());
            statement.setObject(1, primaryKey);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new NoEntityFoundException("Can not get entity from database");
        }
    }

    @Override
    public void delete() {
        var sb = new StringBuilder();
        sb.append("DELETE FROM ").append(getTableName()).append(getWhere());
        try (var conn = connector.getConnection()) {
            var statement = conn.prepareStatement(sb.toString());
            var whereParams = whereQuery.params();
            for (int i = 1; i <= whereParams.size(); i++) {
                statement.setObject(i, whereParams.get(i - 1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resetQuery() {
        whereQuery = null;
        orderClauses.clear();
        limit = -1;
        offset = -1;
    }

    @Override
    public void add(T entity) {
        var tableName = getTableName();

        var sjColumns = new StringJoiner(", ");
        var sjValues = new StringJoiner(", ");
        List<Object> values = new ArrayList<>();

        for (var field : entityClass.getDeclaredFields()) {
            var columnName = field.getName();
            if (field.getType().isAnnotationPresent(Entity.class)) {
                var methodName = field.getName();
                Method method;
                try {
                    method = entityClass.getDeclaredMethod(methodName);
                } catch (NoSuchMethodException e) {
                    try {
                        method = entityClass.getDeclaredMethod("get" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1));
                    } catch (NoSuchMethodException ex) {
                        LOGGER.severe("No getter found for field: " + field.getName() + "(Valid names: get[FieldName] or [fieldName]). Field can not be saved");
                        continue;
                    }
                }

                try {
                    method.setAccessible(true);
                    var foreignEntity = method.invoke(entity);
                    method.setAccessible(false);

                    if (foreignEntity != null) {
                        var entrySet = entityFactory.get(foreignEntity.getClass());
                        entrySet.update(foreignEntity, foreignEntity.getClass());
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            } else if (field.isAnnotationPresent(Id.class)) {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getWhere() {
        if (whereQuery == null) {
            return "";
        }
        var sb = new StringBuilder();
        sb.append(" WHERE ").append(whereQuery.toSql());
        return sb.toString();
    }

    private String getOrderBy() {
        if (orderClauses.isEmpty()) {
            return "";
        }
        var sb = new StringBuilder();
        sb.append(" ORDER BY ");
        var sj = new StringJoiner(", ");
        for (var clause : orderClauses) {
            sj.add(clause.field() + " " + (clause.descending() ? "DESC" : "ASC"));
        }
        sb.append(sj);
        return sb.toString();
    }

    private String getTableName() {
        return entityClass.getAnnotation(Entity.class).value().isEmpty() ? entityClass.getSimpleName() : entityClass.getAnnotation(Entity.class).value();
    }

    private String getPrimaryKeyName() {
        for (var field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return field.getAnnotation(Id.class).name().isEmpty() ? field.getName() : field.getAnnotation(Id.class).name();
            }
        }

        throw new IllegalArgumentException(entityClass.getName() + " has no primary key");
    }

    private T mapToEntity(ResultSet rs) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        var entity = entityClass.getDeclaredConstructor().newInstance();

        for (var field : entityClass.getDeclaredFields()) {
            var fieldName = field.getName();
            if (field.isAnnotationPresent(Id.class)) {
                fieldName = field.getAnnotation(Id.class).name().isEmpty() ? fieldName : field.getAnnotation(Id.class).name();
            } else if (field.isAnnotationPresent(Column.class)) {
                fieldName = field.getAnnotation(Column.class).name().isEmpty() ? fieldName : field.getAnnotation(Column.class).name();
            }

            // TODO: handle lazy

            field.setAccessible(true);
            Object value;

            if (field.getType().isEnum()) {
                value = Enum.valueOf((Class<? extends Enum>) field.getType(), rs.getString(fieldName));
            } else {
                value = rs.getObject(fieldName);
            }
            field.set(entity, value);
            field.setAccessible(false);
        }

        tracker.snapshot(entity);

        return entity;
    }
}

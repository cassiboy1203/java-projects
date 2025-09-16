package com.yukikase.framework.orm.entity;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.yukikase.framework.exceptions.NoEntityFoundException;
import com.yukikase.framework.orm.IDatabaseConnector;

import java.sql.SQLException;
import java.util.List;

public class OrmLiteEntitySet<T> implements EntitySet<T> {

    private final Class<T> entityClass;
    private final Dao<T, Object> dao;

    public OrmLiteEntitySet(Class<T> entityClass, IDatabaseConnector connector) {
        this.entityClass = entityClass;

        try {
            this.dao = DaoManager.createDao(connector.getConnection(), entityClass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public QueryBuilder<T, Object> queryBuilder() {
        return dao.queryBuilder();
    }

    @Override
    public List<T> query(PreparedQuery<T> preparedQuery) {
        try {
            return dao.query(preparedQuery);
        } catch (SQLException e) {
            throw new NoEntityFoundException("No entity found for given query");
        }
    }

    @Override
    public T get(Object primaryKey) {
        try {
            return dao.queryForId(primaryKey);
        } catch (SQLException e) {
            throw new NoEntityFoundException("No entity found for primary key: " + primaryKey, e);
        }
    }

    @Override
    public List<T> getAll() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new NoEntityFoundException("No entity found of type " + entityClass.getName(), e);
        }
    }

    @Override
    public long count() {
        try {
            return dao.countOf();
        } catch (SQLException e) {
            throw new NoEntityFoundException("Something went wrong while counting entities", e);
        }
    }

    @Override
    public void update(T entity) {
        try {
            dao.update(entity);
        } catch (SQLException e) {
            throw new NoEntityFoundException("Something went wrong while updating entity", e);
        }
    }

    @Override
    public void delete(T entity) {
        try {
            dao.delete(entity);
        } catch (SQLException e) {
            throw new NoEntityFoundException("Something went wrong while deleting entity", e);
        }
    }

    @Override
    public void add(T entity) {
        try {
            dao.create(entity);
        } catch (SQLException e) {
            throw new NoEntityFoundException("Something went wrong while creating entity", e);
        }
    }
}

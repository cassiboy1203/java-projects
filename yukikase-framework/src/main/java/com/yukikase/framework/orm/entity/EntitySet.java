package com.yukikase.framework.orm.entity;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.util.List;

public interface EntitySet<T> {
    QueryBuilder<T, Object> queryBuilder();

    List<T> query(PreparedQuery<T> preparedQuery);

    T get(Object primaryKey);

    List<T> getAll();

    long count();

    void update(T entity);

    void delete(T entity);

    void add(T entity);

    record OrderClause(String field, boolean descending) {
    }
}

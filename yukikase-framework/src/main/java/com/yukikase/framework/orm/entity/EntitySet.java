package com.yukikase.framework.orm.entity;

import com.yukikase.framework.orm.query.Query;

import java.util.Set;

public interface EntitySet<T> {
    EntitySet<T> where(Query query);

    EntitySet<T> orderBy(String field);

    EntitySet<T> orderBy(String field, boolean descending);

    EntitySet<T> limit(int limit);

    EntitySet<T> offset(int offset);

    T get(Object primaryKey);

    Set<T> getAll();

    Set<T> getAllNext();

    T next();

    int count();

    void update(T entity);

    void delete(T entity);

    void delete();

    void resetQuery();

    record OrderClause(String field, boolean descending) {
    }
}

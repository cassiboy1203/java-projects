package com.yukikase.framework.orm.entity;

import java.sql.SQLException;

public interface EntityFactory {
    <T> void save(T entity) throws SQLException;

    <T> EntitySet<T> get(Class<T> entityClass);
}

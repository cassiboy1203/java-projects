package com.yukikase.framework.orm;

import com.yukikase.framework.orm.mysql.MySQLTableGenerator;

import java.util.Set;

public interface ITableGenerator {

    static ITableGenerator getInstance(DatabaseType type, IDatabaseConnector connector) {
        return switch (type) {
            case MYSQL -> new MySQLTableGenerator(connector);
        };
    }

    String updateTables(Set<Class<?>> classes);

    void updateTable(Class<?> clazz);

    void generateTable(Class<?> clazz);

    String fromJavaType(Class<?> fieldType);

    String fromJavaType(Class<?> fieldType, int length);
}

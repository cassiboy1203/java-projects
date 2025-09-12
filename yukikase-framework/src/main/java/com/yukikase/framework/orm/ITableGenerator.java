package com.yukikase.framework.orm;

import com.yukikase.framework.orm.mysql.MySQLTableGenerator;

public interface ITableGenerator {

    static ITableGenerator getInstance(DatabaseType type, IDatabaseConnector connector) {
        return switch (type) {
            case MYSQL -> new MySQLTableGenerator(connector);
        };
    }

    String updateTable(Class<?> clazz);

    String generateTable(Class<?> clazz);

    String fromJavaType(Class<?> fieldType);

    String fromJavaType(Class<?> fieldType, int length);
}

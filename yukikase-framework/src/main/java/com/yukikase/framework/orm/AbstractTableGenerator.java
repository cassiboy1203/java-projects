package com.yukikase.framework.orm;

import com.yukikase.framework.anotations.NonNull;
import com.yukikase.framework.anotations.orm.Column;
import com.yukikase.framework.anotations.orm.Entity;
import com.yukikase.framework.anotations.orm.Id;
import com.yukikase.framework.exceptions.EntityCreationException;

import java.lang.reflect.Field;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;
import java.util.logging.Logger;

public abstract class AbstractTableGenerator implements ITableGenerator {
    private final IDatabaseConnector connector;
    protected static final Logger LOGGER = Logger.getLogger(AbstractTableGenerator.class.getName());

    protected final TableRelationNode rootTableRelationNode = new TableRelationNode();

    protected AbstractTableGenerator(IDatabaseConnector connector) {
        this.connector = connector;
    }

    @Override
    public final String updateTables(Set<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            updateTable(clazz);
        }
        return rootTableRelationNode.toSql();
    }

    @Override
    public final void updateTable(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new RuntimeException("Class " + clazz.getName() + " is not an entity");
        }

        var tableName = tableName(clazz);

        Map<String, ColumnInfo> columnInfoMap = new HashMap<>();
        try (var conn = connector.getConnection()) {
            var meta = conn.getMetaData();

            Set<String> primaryKeys = new HashSet<>();
            try (var rs = meta.getPrimaryKeys(null, null, tableName)) {
                while (rs.next()) {
                    primaryKeys.add(rs.getString("COLUMN_NAME"));
                }
            }

            try (var rs = meta.getColumns(null, null, tableName, null)) {
                while (rs.next()) {
                    var name = rs.getString("COLUMN_NAME");
                    var type = rs.getInt("DATA_TYPE");
                    var size = rs.getInt("COLUMN_SIZE");
                    var nullableFlag = rs.getInt("NULLABLE");
                    var nullable = (nullableFlag == DatabaseMetaData.columnNullable);

                    var primaryKey = (primaryKeys.contains(name));

                    columnInfoMap.put(name, new ColumnInfo(type, nullable, primaryKey, size));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        if (columnInfoMap.isEmpty()) {
            generateTable(clazz);
            return;
        }

        var node = rootTableRelationNode.getNode(clazz);
        if (node == null) {
            node = new TableRelationNode(clazz);
        }

        var sj = new StringJoiner("\n\n");

        for (var field : clazz.getDeclaredFields()) {
            var columnName = columnName(field);

            if (columnInfoMap.containsKey(columnName)) {
                var columnInfo = columnInfoMap.remove(columnName);

                if (field.getType().isAnnotationPresent(Entity.class)) {
                    // TODO: handle foreign key updates
                } else if (!isSameType(columnInfo.type(), field.getType())) {
                    sj.add(alterColumnType(clazz, field));
                } else if (hasVariableSize(field.getType())) {
                    var size = getSize(field);
                    if (size > 0 && columnInfo.size() != size) {
                        sj.add(alterColumnType(clazz, field));
                    }
                }

                if (columnInfo.nullable() == isNonNull(field)) {
                    if (!field.getType().isAnnotationPresent(Entity.class)) {
                        sj.add(alterNullability(clazz, field));
                    }
                }

                if (columnInfo.primaryKey() != isPrimaryKey(field)) {
                    throw new EntityCreationException("Primary key from entity: " + tableName + ". Is different than in the database. Please manually update primary key.");
                }
            } else {
                sj.add(addNewColumn(clazz, node, field));
            }
        }
        if (!columnInfoMap.isEmpty()) {
            var sb = new StringBuilder();
            sb.append("The following columns in table: ").append(tableName).append(" are no longer in use.");
            var sj2 = new StringJoiner("\n - ", "\n - ", "");
            for (var column : columnInfoMap.entrySet()) {
                sj2.add(column.getKey());
                sj.add(removeColumn(clazz, column.getKey(), fromJdbcType(column.getValue().type(), column.getValue().size())));
            }
            sb.append(sj2);
            sb.append("\n");
            sb.append("Please manually remove them if you no longer need them.");

            LOGGER.warning(sb.toString());
        }

        node.setSql(sj.toString());

        if (!rootTableRelationNode.contains(clazz)) {
            rootTableRelationNode.addDependentNode(node);
        }
    }

    protected final int getSize(Field field) {
        int length = -1;
        if (!hasVariableSize(field.getType())) {
            return length;
        } else if (field.isAnnotationPresent(Id.class)) {
            var id = field.getAnnotation(Id.class);
            length = id.length();
        } else if (field.isAnnotationPresent(Column.class)) {
            var column = field.getAnnotation(Column.class);
            length = column.length();
        }

        return length;
    }

    protected final String tableName(Class<?> clazz) {
        var entity = clazz.getAnnotation(Entity.class);
        return entity.value().isEmpty() ? clazz.getSimpleName() : entity.value();
    }

    protected final String columnName(Field field) {
        String columnName = field.getName();
        if (field.isAnnotationPresent(Id.class)) {
            var id = field.getAnnotation(Id.class);
            columnName = id.name().isEmpty() ? columnName : id.name();
        } else if (field.isAnnotationPresent(Column.class)) {
            var column = field.getAnnotation(Column.class);
            columnName = column.name().isEmpty() ? field.getName() : column.name();
        }

        return columnName;
    }

    protected final boolean isNonNull(Field field) {
        return field.isAnnotationPresent(NonNull.class) || field.isAnnotationPresent(Id.class);
    }

    protected final boolean isPrimaryKey(Field field) {
        return field.isAnnotationPresent(Id.class);
    }

    protected abstract boolean hasVariableSize(Class<?> type);

    protected abstract String alterColumnType(Class<?> entityClass, Field field);

    protected abstract String addNewColumn(Class<?> entityClass, TableRelationNode node, Field field);

    protected abstract String alterNullability(Class<?> entityClass, Field field);

    protected abstract String removeColumn(Class<?> entityClass, String columnName, String type);

    protected abstract String fromJdbcType(int jdbcType, int length);

    protected final String fieldSqlType(Field field) {
        var length = getSize(field);
        return fromJavaType(field.getType(), length);
    }

    protected boolean isSameType(int jdbcType, Class<?> javaType) {
        switch (jdbcType) {
            case Types.VARCHAR -> {
                if (javaType.equals(String.class)) return true;
                if (javaType.isEnum()) return true;

            }
            case Types.INTEGER -> {
                if (javaType.equals(int.class)) return true;
                if (javaType.equals(Integer.class)) return true;
            }
            case Types.BIGINT -> {
                if (javaType.equals(long.class)) return true;
                if (javaType.equals(Long.class)) return true;
            }
            case Types.DOUBLE -> {
                if (javaType.equals(double.class)) return true;
                if (javaType.equals(Double.class)) return true;
            }
            case Types.FLOAT -> {
                if (javaType.equals(float.class)) return true;
                if (javaType.equals(Float.class)) return true;
            }
            case Types.BOOLEAN -> {
                if (javaType.equals(boolean.class)) return true;
                if (javaType.equals(Boolean.class)) return true;
            }
            case Types.DATE -> {
                if (javaType.equals(Date.class)) return true;
            }
            case Types.CHAR -> {
                if (javaType.equals(UUID.class)) return true;
            }
            default -> {
                return true;
            }
        }

        throw new IllegalArgumentException("Field type " + javaType.getName() + " is not supported");
    }

    protected final String defaultValue(Class<?> fieldType) {
        if (fieldType.equals(String.class)) return "";
        if (fieldType.isEnum()) return "";
        if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) return "0";
        if (fieldType.equals(long.class) || fieldType.equals(Long.class)) return "0";
        if (fieldType.equals(double.class) || fieldType.equals(Double.class)) return "0.0";
        if (fieldType.equals(float.class) || fieldType.equals(Float.class)) return "0.0";
        if (fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)) return "0";
        if (fieldType.equals(Date.class)) return "";
        if (fieldType.equals(UUID.class)) return "";

        return null;
    }

    protected record ColumnInfo(int type, boolean nullable, boolean primaryKey, int size) {
    }
}

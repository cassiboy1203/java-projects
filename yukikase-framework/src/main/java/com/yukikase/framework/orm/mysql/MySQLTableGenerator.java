package com.yukikase.framework.orm.mysql;

import com.yukikase.framework.anotations.NonNull;
import com.yukikase.framework.anotations.injection.Component;
import com.yukikase.framework.anotations.injection.Inject;
import com.yukikase.framework.anotations.orm.Entity;
import com.yukikase.framework.anotations.orm.Id;
import com.yukikase.framework.orm.AbstractTableGenerator;
import com.yukikase.framework.orm.IDatabaseConnector;
import com.yukikase.framework.orm.TableRelationNode;

import java.lang.reflect.Field;
import java.sql.Types;
import java.util.*;

@Component
public class MySQLTableGenerator extends AbstractTableGenerator {

    @Inject
    public MySQLTableGenerator(IDatabaseConnector connector) {
        super(connector);
    }

    @Override
    protected boolean hasVariableSize(Class<?> type) {
        if (type.equals(String.class)) {
            return true;
        }

        return false;
    }

    @Override
    protected String alterColumnType(Class<?> entityClass, Field field) {
        var sb = new StringBuilder();
        var tableName = tableName(entityClass);
        var columnName = columnName(field);
        var type = fromJavaType(field.getType(), getSize(field));

        sb.append("ALTER TABLE ").append(tableName).append(" MODIFY COLUMN ").append(columnName).append(" ").append(type);

        return sb.toString();
    }

    @Override
    protected String addNewColumn(Class<?> entityClass, TableRelationNode node, Field field) {
        var sb = new StringBuilder();
        var tableName = tableName(entityClass);
        var columnName = columnName(field);
        String type = null;
        List<String> foreignKeys = new ArrayList<>();
        if (field.getType().isAnnotationPresent(Entity.class)) {
            for (var dependantField : field.getType().getDeclaredFields()) {
                if (dependantField.isAnnotationPresent(Id.class)) {
                    var foreignFieldName = columnName(dependantField);
                    type = fromJavaType(dependantField.getType(), getSize(dependantField));

                    foreignKeys.add("ALTER TABLE " + tableName + " ADD CONSTRAINT fk_" + tableName + "_" + columnName + " FOREIGN KEY (" + columnName + ") REFERENCES " + tableName(field.getType()) + "(" + foreignFieldName + ")");
                    break;
                }
            }
            if (type == null) {
                throw new IllegalArgumentException("Entity " + field.getType().getName() + " does not have a primary key");
            }

            var dependantNode = rootTableRelationNode.getNode(field.getType());
            if (dependantNode == null) {
                dependantNode = new TableRelationNode(field.getType());
                rootTableRelationNode.addDependentNode(dependantNode);
            }
            dependantNode.addDependentNode(node);
        } else {
            type = fromJavaType(field.getType(), getSize(field));
        }
        sb.append("ALTER TABLE ").append(tableName).append(" ADD COLUMN ").append(columnName).append(" ").append(type);
        if (isNonNull(field)) {
            sb.append("\n").append("UPDATE ").append(tableName).append(" SET ").append(columnName).append(" = ").append(defaultValue(field.getType()));
            sb.append("ALTER TABLE ").append(tableName).append(" MODIFY COLUMN ").append(columnName).append(" ").append(type).append(" NOT NULL");
        }

        sb.append("\n").append(foreignKeys);

        return sb.toString();
    }

    @Override
    protected String alterNullability(Class<?> entityClass, Field field) {
        var sb = new StringBuilder();
        var tableName = tableName(entityClass);
        var columnName = columnName(field);
        var type = fromJavaType(field.getType(), getSize(field));
        if (isNonNull(field)) {
            sb.append("\n").append("UPDATE ").append(tableName).append(" SET ").append(columnName).append(" = ").append(defaultValue(field.getType()));
            sb.append("ALTER TABLE ").append(tableName).append(" MODIFY COLUMN ").append(columnName).append(" ").append(type).append(" NOT NULL");
        } else {
            sb.append("ALTER TABLE ").append(tableName).append(" MODIFY COLUMN ").append(columnName).append(" ").append(type);
        }

        return sb.toString();
    }

    @Override
    protected String removeColumn(Class<?> entityClass, String columnName, String type) {
        var sb = new StringBuilder();
        var tableName = tableName(entityClass);

        sb.append("ALTER TABLE ").append(tableName).append(" MODIFY COLUMN").append(columnName).append(" ").append(type);
        return sb.toString();
    }

    @Override
    protected String fromJdbcType(int jdbcType, int length) {
        return switch (jdbcType) {
            case Types.VARCHAR -> "VARCHAR(" + length + ")";
            case Types.CHAR -> "CHAR(" + length + ")";
            case Types.INTEGER -> "INT";
            case Types.BIGINT -> "BIGINT";
            case Types.DOUBLE -> "DOUBLE";
            case Types.FLOAT -> "FLOAT";
            case Types.DATE -> "DATETIME";
            case Types.BOOLEAN -> "BOOLEAN";
            default -> "VARCHAR(" + length + ")";
        };
    }


    @Override
    public void generateTable(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " is not an entity");
        }

        var node = rootTableRelationNode.getNode(clazz);
        if (node == null) {
            node = new TableRelationNode(clazz);
        }

        var tableName = tableName(clazz);

        var sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append("(\n");

        var fields = clazz.getDeclaredFields();

        var sj = new StringJoiner(",\n");
        List<String> foreignKeys = new ArrayList<>();
        for (var field : fields) {
            var columnName = columnName(field);
            String sqlType = null;
            if (field.getType().isAnnotationPresent(Entity.class)) {
                for (var dependantField : field.getType().getDeclaredFields()) {
                    if (dependantField.isAnnotationPresent(Id.class)) {
                        var foreignFieldName = columnName(dependantField);
                        sqlType = fromJavaType(dependantField.getType(), getSize(dependantField));

                        foreignKeys.add("FOREIGN KEY (" + columnName + ") references " + tableName(field.getType()) + "(" + foreignFieldName + ")");
                        break;
                    }
                }
                if (sqlType == null) {
                    throw new IllegalArgumentException("Entity " + field.getType().getName() + " does not have a primary key");
                }

                var dependantNode = rootTableRelationNode.getNode(field.getType());
                if (dependantNode == null) {
                    dependantNode = new TableRelationNode(field.getType());
                    rootTableRelationNode.addDependentNode(dependantNode);
                }
                dependantNode.addDependentNode(node);
            } else {
                sqlType = fromJavaType(field.getType(), getSize(field));
            }

            var sb2 = new StringBuilder();

            sb2.append("\t").append(columnName).append(" ").append(sqlType);

            if (field.isAnnotationPresent(Id.class)) {
                sb2.append(" PRIMARY KEY");
            }

            if (field.isAnnotationPresent(NonNull.class)) {
                sb2.append(" NOT NULL");
            }

            sj.add(sb2.toString());
        }
        for (var foreignKey : foreignKeys) {
            sj.add("\t" + foreignKey);
        }

        sb.append(sj);
        sb.append("\n);");

        node.setSql(sb.toString());

        if (!rootTableRelationNode.contains(clazz)) {
            rootTableRelationNode.addDependentNode(node);
        }
    }

    @Override
    public String fromJavaType(Class<?> fieldType) {
        return fromJavaType(fieldType, 255);
    }

    @Override
    public String fromJavaType(Class<?> fieldType, int length) {
        if (fieldType.equals(String.class)) return "VARCHAR(" + length + ")";
        if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) return "INT";
        if (fieldType.equals(long.class) || fieldType.equals(Long.class)) return "BIGINT";
        if (fieldType.equals(double.class) || fieldType.equals(Double.class)) return "DOUBLE";
        if (fieldType.equals(float.class) || fieldType.equals(Float.class)) return "FLOAT";
        if (fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)) return "BOOLEAN";
        if (fieldType.equals(UUID.class)) return "CHAR(36)";
        if (fieldType.isEnum()) return "VARCHAR(100)";
        if (fieldType.equals(Date.class)) return "DATETIME";

        throw new IllegalArgumentException("Field type " + fieldType.getName() + " is not supported");
    }

}
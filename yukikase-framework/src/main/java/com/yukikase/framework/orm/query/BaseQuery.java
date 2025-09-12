package com.yukikase.framework.orm.query;

import java.util.List;

public class BaseQuery implements Query {
    private final QueryType queryType;
    private final String field;
    private final Object value;

    BaseQuery(QueryType queryType, String field, Object value) {
        this.queryType = queryType;
        this.field = field;
        this.value = value;
    }

    @Override
    public String toSql() {
        var sb = new StringBuilder();
        sb.append(field);
        switch (queryType) {
            case EQUALS -> {
                sb.append(" = ");
            }
            case NOT_EQUALS -> {
                sb.append(" NOT ");
            }
            case GREATER_THAN -> {
                sb.append(" > ");
            }
            case LESS_THAN -> {
                sb.append(" < ");
            }
            case GREATER_OR_EQUAL -> {
                sb.append(" >= ");
            }
            case LESS_OR_EQUAL -> {
                sb.append(" <= ");
            }
            case LIKE -> {
                sb.append(" LIKE ");
            }
            default -> {
                throw new RuntimeException("Unsupported query type: " + queryType);
            }
        }

        sb.append(" ?");

        return sb.toString();
    }

    @Override
    public List<Object> params() {
        return List.of(value);
    }
}

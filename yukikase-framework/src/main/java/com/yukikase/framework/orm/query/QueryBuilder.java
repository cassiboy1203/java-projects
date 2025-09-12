package com.yukikase.framework.orm.query;

public class QueryBuilder {
    private QueryBuilder() {
    }

    public static Query equals(String field, Object value) {
        return new BaseQuery(QueryType.EQUALS, field, value);
    }

    public static Query notEquals(String field, Object value) {
        return new BaseQuery(QueryType.NOT_EQUALS, field, value);
    }

    public static Query like(String field, String value) {
        return new BaseQuery(QueryType.LIKE, field, value);
    }

    public static Query greaterThan(String field, Object value) {
        return new BaseQuery(QueryType.GREATER_THAN, field, value);
    }

    public static Query greaterThanOrEquals(String field, Object value) {
        return new BaseQuery(QueryType.GREATER_OR_EQUAL, field, value);
    }

    public static Query lessThan(String field, Object value) {
        return new BaseQuery(QueryType.LESS_THAN, field, value);
    }

    public static Query lessThanOrEquals(String field, Object value) {
        return new BaseQuery(QueryType.LESS_OR_EQUAL, field, value);
    }

    public static Query in(String field, Object... values) {
        return new BaseQuery(QueryType.IN, field, values);
    }

    public static Query notIn(String field, Object... values) {
        return new BaseQuery(QueryType.NOT_IN, field, values);
    }

    public static Query or(Query q1, Query q2) {
        return new OrQuery(q1, q2);
    }

    public static Query and(Query q1, Query q2) {
        return new AndQuery(q1, q2);
    }
}

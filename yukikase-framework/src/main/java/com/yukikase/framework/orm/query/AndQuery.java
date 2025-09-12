package com.yukikase.framework.orm.query;

import java.util.List;

public class AndQuery implements Query {
    private Query query1;
    private Query query2;

    public AndQuery(Query query1, Query query2) {
        this.query1 = query1;
        this.query2 = query2;
    }

    @Override
    public String toSql() {
        return query1.toSql() + " AND " + query2.toSql() + " ";
    }

    @Override
    public List<Object> params() {
        var list = query1.params();
        list.addAll(query2.params());
        return list;
    }
}

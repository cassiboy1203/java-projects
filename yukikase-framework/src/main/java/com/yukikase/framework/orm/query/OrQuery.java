package com.yukikase.framework.orm.query;

import java.util.List;

public class OrQuery implements Query {
    private final Query query1;
    private final Query query2;

    public OrQuery(Query query1, Query query2) {
        this.query1 = query1;
        this.query2 = query2;
    }

    @Override
    public String toSql() {
        return query1.toSql() + " OR " + query2.toSql() + " ";
    }

    @Override
    public List<Object> params() {
        var list = query1.params();
        list.addAll(query2.params());
        return list;
    }
}

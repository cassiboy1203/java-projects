package com.yukikase.framework.orm.query;

import java.util.List;

public interface Query {
    String toSql();

    List<Object> params();
}

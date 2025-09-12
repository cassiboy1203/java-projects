package com.yukikase.framework.orm.testclasses;

import com.yukikase.framework.anotations.NonNull;
import com.yukikase.framework.anotations.orm.Column;
import com.yukikase.framework.anotations.orm.Entity;
import com.yukikase.framework.anotations.orm.Id;

@Entity
public class TestEntity {
    @Id
    private int integer;

    private String string;

    @Column(length = 100)
    private String lengthString;

    @Column(name = "test")
    private boolean bool;

    @NonNull
    private String nonNullString;
}

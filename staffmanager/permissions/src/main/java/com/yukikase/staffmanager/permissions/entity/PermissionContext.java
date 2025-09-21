package com.yukikase.staffmanager.permissions.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.yukikase.staffmanager.permissions.PermissionContexts;

@DatabaseTable
public class PermissionContext {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private PermissionContexts context;

    @DatabaseField
    private String value;
}

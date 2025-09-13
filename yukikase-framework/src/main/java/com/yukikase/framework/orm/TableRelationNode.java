package com.yukikase.framework.orm;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class TableRelationNode {
    private final Class<?> clazz;
    private String sql;
    private final List<TableRelationNode> dependentNodes = new ArrayList<>();
    private final boolean isRoot;
    private static List<Class<?>> passedClasses = new ArrayList<>();

    public TableRelationNode() {
        this.clazz = null;
        this.sql = null;
        this.isRoot = true;
    }

    public TableRelationNode(Class<?> clazz) {
        this.clazz = clazz;
        this.isRoot = false;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void addDependentNode(TableRelationNode node) {
        dependentNodes.add(node);
    }

    public TableRelationNode getNode(Class<?> clazz) {
        if (!isRoot && Objects.equals(this.clazz, clazz)) {
            return this;
        }

        for (TableRelationNode node : dependentNodes) {
            var returnNode = node.getNode(clazz);
            if (returnNode != null) {
                return returnNode;
            }
        }

        return null;
    }

    public boolean contains(Class<?> clazz) {
        if (Objects.equals(clazz, this.clazz)) {
            return true;
        }

        for (TableRelationNode node : dependentNodes) {
            if (node.contains(clazz)) {
                return true;
            }
        }

        return false;
    }

    public String toSql() {
        var sj = new StringJoiner("\n\n");
        if (isRoot) {
            passedClasses.clear();
        } else if (!passedClasses.contains(clazz)) {
            sj.add(sql);
            passedClasses.add(this.clazz);
        }

        for (var node : dependentNodes) {
            sj.add(node.toSql());
        }

        return sj.toString();
    }
}

package com.yukikase.framework.orm.entity;

public record EntityKey(Object entity) {
    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof EntityKey(Object entity1) && this.entity == entity1);
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(entity);
    }
}

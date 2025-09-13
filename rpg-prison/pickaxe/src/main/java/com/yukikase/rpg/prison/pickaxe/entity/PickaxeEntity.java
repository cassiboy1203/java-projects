package com.yukikase.rpg.prison.pickaxe.entity;

import com.yukikase.framework.anotations.orm.Column;
import com.yukikase.framework.anotations.orm.Entity;
import com.yukikase.framework.anotations.orm.Id;

import java.util.UUID;

@Entity("Pickaxe")
public class PickaxeEntity {
    @Id
    UUID owner;
    int level;
    long experience;
    @Column(length = 10)
    PickaxeMaterial material;

    public PickaxeEntity() {
    }

    public PickaxeEntity(UUID owner, int level, long experience, PickaxeMaterial material) {
        this.owner = owner;
        this.level = level;
        this.experience = experience;
        this.material = material;
    }

    public UUID owner() {
        return owner;
    }

    public int level() {
        return level;
    }

    public long experience() {
        return experience;
    }

    public PickaxeMaterial material() {
        return material;
    }

    public void level(int level) {
        this.level = level;
    }

    public void experience(long experience) {
        this.experience = experience;
    }

    public void material(PickaxeMaterial material) {
        this.material = material;
    }
}

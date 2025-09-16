package com.yukikase.rpg.prison.pickaxe.timer;

import com.yukikase.lib.task.Task;
import org.bukkit.Location;
import org.bukkit.Material;

public class BlockRespawnTask extends Task {

    private final Location location;
    private final Material material;
    private final int respawnTime;

    public BlockRespawnTask(Location location, Material material, int respawnTime) {
        this.location = location;
        this.material = material;
        this.respawnTime = respawnTime;
    }

    @Override
    public long getDelay() {
        return respawnTime * 20L;
    }

    @Override
    public void run() {
        location.getBlock().setType(material);
    }

    @Override
    public void onCancel() {
        location.getBlock().setType(material);
    }
}

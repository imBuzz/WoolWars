package me.buzz.woolwars.game.game.arena.regions;

import me.buzz.woolwars.api.game.arena.region.Region;
import me.buzz.woolwars.game.game.arena.location.ArenaLocation;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class CuboidRegion implements Region {

    private final int topBlockX, bottomBlockX, topBlockY, bottomBlockY, topBlockZ, bottomBlockZ;
    private World world;

    public CuboidRegion(ArenaLocation min, ArenaLocation max) {
        topBlockX = (int) Math.max(max.getX(), min.getX());
        bottomBlockX = (int) Math.min(max.getX(), min.getX());

        topBlockY = (int) (Math.max(max.getY(), min.getY()));
        bottomBlockY = (int) (Math.min(max.getY(), min.getY()));

        topBlockZ = (int) (Math.max(max.getZ(), min.getZ()));
        bottomBlockZ = (int) (Math.min(max.getZ(), min.getZ()));
    }

    @Override
    public void setWorld(World world) {
        this.world = world;
    }

    public boolean isInRegion(Location location) {
        return location.getX() >= bottomBlockX && location.getX() <= topBlockX &&
                location.getY() >= bottomBlockY && location.getY() <= topBlockY &&
                location.getZ() >= bottomBlockZ && location.getZ() <= topBlockZ;
    }

    public List<Block> getBlocks() {
        List<Block> blocks = new ArrayList<>();

        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                for (int y = bottomBlockY; y <= topBlockY; y++) {
                    blocks.add(world.getBlockAt(x, y, z));
                }
            }
        }

        return blocks;
    }

}

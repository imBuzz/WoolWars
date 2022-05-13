package me.buzz.woolwars.game.game.arena.arena.regions;

import lombok.RequiredArgsConstructor;
import me.buzz.woolwars.api.game.arena.region.Region;
import me.buzz.woolwars.game.game.arena.location.ArenaLocation;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CuboidRegion implements Region {

    private final ArenaLocation min, max;
    private World world;

    @Override
    public void setWorld(World world) {
        this.world = world;
    }

    public boolean isInRegion(Location location) {
        return location.getX() >= min.getX() && location.getX() <= max.getX() &&
                location.getY() >= min.getY() && location.getY() <= max.getY() &&
                location.getZ() >= min.getZ() && location.getZ() <= max.getZ();
    }

    public List<Block> getBlocks() {
        double lowX = Math.min(min.getX(), max.getX());
        double lowY = Math.min(min.getY(), max.getY());
        double lowZ = Math.min(min.getZ(), max.getZ());

        List<Block> blocks = new ArrayList<>();

        for (int x = 0; x < Math.abs(min.getX() - max.getX()); x++) {
            for (int y = 0; y < Math.abs(min.getY() - max.getY()); y++) {
                for (int z = 0; z < Math.abs(min.getZ() - max.getZ()); z++) {
                    blocks.add(new Location(world, lowX + x, lowY + y, lowZ + z).getBlock());
                }
            }
        }

        return blocks;
    }

}

package me.buzz.woolwars.api.game.arena.region;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.List;

public interface Region {

    boolean isInRegion(Location location);

    List<Block> getBlocks();

}

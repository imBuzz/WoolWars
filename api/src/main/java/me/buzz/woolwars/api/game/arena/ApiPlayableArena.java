package me.buzz.woolwars.api.game.arena;

import me.buzz.woolwars.api.game.arena.region.ArenaRegionType;
import me.buzz.woolwars.api.game.arena.region.Region;
import org.bukkit.Location;
import org.bukkit.World;

public interface ApiPlayableArena {

    String getID();

    String getName();

    World getWorld();

    Location getLocation(ArenaLocationType type);

    Region getRegion(ArenaRegionType type);

}

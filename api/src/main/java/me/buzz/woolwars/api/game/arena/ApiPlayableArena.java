package me.buzz.woolwars.api.game.arena;

import org.bukkit.Location;
import org.bukkit.World;

public interface ApiPlayableArena {

    String getID();

    String getName();

    World getWorld();

    Location getLocation(ArenaLocationType type);

}

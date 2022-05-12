package me.buzz.woolwars.game.game.arena.arena;

import me.buzz.woolwars.api.game.arena.ApiPlayableArena;
import me.buzz.woolwars.api.game.arena.ArenaLocationType;
import org.bukkit.Location;
import org.bukkit.World;

public class PlayableArena extends ArenaMetadata implements ApiPlayableArena {

    private final World world;

    public PlayableArena(ArenaMetadata metadata, World world) {
        super(metadata.ID, metadata.name, metadata.locations, metadata.regions);
        this.world = world;
    }

    @Override
    public Location getLocation(ArenaLocationType type) {
        return getArenaLocation(type).toBukkitLocation(world).clone();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public World getWorld() {
        return world;
    }
}

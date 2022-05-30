package me.buzz.woolwars.game.game.arena;

import me.buzz.woolwars.api.game.arena.ApiPlayableArena;
import me.buzz.woolwars.api.game.arena.ArenaLocationType;
import me.buzz.woolwars.api.game.arena.region.ArenaRegionType;
import me.buzz.woolwars.api.game.arena.region.Region;
import me.buzz.woolwars.game.game.arena.regions.ImplementedRegion;
import org.bukkit.Location;
import org.bukkit.World;

public class PlayableArena extends ArenaMetadata implements ApiPlayableArena {

    private final World world;

    public PlayableArena(ArenaMetadata metadata, World world) {
        super(metadata.ID, metadata.name, metadata.matchType, metadata.powerups, metadata.locations, metadata.regions, metadata.getWorldName(), metadata.presets);
        this.world = world;

        for (ImplementedRegion value : regions.values()) {
            value.setWorld(this.world);
        }
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

    @Override
    public Region getRegion(ArenaRegionType type) {
        return regions.get(type);
    }
}

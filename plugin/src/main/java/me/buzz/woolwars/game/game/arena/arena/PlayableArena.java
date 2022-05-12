package me.buzz.woolwars.game.game.arena.arena;

import me.buzz.woolwars.game.game.arena.location.ArenaLocationType;
import org.bukkit.Location;
import org.bukkit.World;

public class PlayableArena extends ArenaMetadata {

    private final World world;

    public PlayableArena(ArenaMetadata metadata, World world) {
        super(metadata.ID, metadata.name, metadata.locations);
        this.world = world;
    }

    public Location getBukkitLocation(ArenaLocationType type){
        return getArenaLocation(type).toBukkitLocation(world);
    }

}

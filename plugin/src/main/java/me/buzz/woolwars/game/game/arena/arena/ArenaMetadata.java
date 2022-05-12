package me.buzz.woolwars.game.game.arena.arena;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.buzz.woolwars.api.game.arena.ArenaLocationType;
import me.buzz.woolwars.game.game.arena.location.ArenaLocation;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ArenaMetadata {

    @Getter protected final String ID;
    protected final String name;
    protected final Map<ArenaLocationType, ArenaLocation> locations;

    public ArenaLocation getArenaLocation(ArenaLocationType type){
        return locations.get(type);
    }

    public PlayableArena toPlayableArena(World world){
        return new PlayableArena(this, world);
    }

    public static ArenaMetadata fromFile(File file){
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);

        String ID = FilenameUtils.removeExtension(file.getName());
        String name = data.getString("name");

        Map<ArenaLocationType, ArenaLocation> locations = new HashMap<>();

        locations.put(ArenaLocationType.WAITING_LOBBY, ArenaLocation.fromString(data.getString("locations.spawns.waitingLobby")));

        locations.put(ArenaLocationType.SPAWN_RED, ArenaLocation.fromString(data.getString("locations.spawns.teamA")));
        locations.put(ArenaLocationType.SPAWN_BLUE, ArenaLocation.fromString(data.getString("locations.spawns.teamB")));

        locations.put(ArenaLocationType.POWERUP_1, ArenaLocation.fromString(data.getString("locations.powerups.1")));
        locations.put(ArenaLocationType.POWERUP_2, ArenaLocation.fromString(data.getString("locations.powerups.2")));

        return new ArenaMetadata(ID, name, locations);
    }


}

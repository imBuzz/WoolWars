package me.buzz.woolwars.game.game.arena.arena;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.buzz.woolwars.api.game.arena.ArenaLocationType;
import me.buzz.woolwars.api.game.arena.region.ArenaRegionType;
import me.buzz.woolwars.api.game.arena.region.Region;
import me.buzz.woolwars.game.game.arena.arena.regions.CuboidRegion;
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
    protected final Map<ArenaRegionType, Region> regions;

    public ArenaLocation getArenaLocation(ArenaLocationType type){
        return locations.get(type);
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

        Map<ArenaRegionType, Region> regions = new HashMap<>();

        regions.put(ArenaRegionType.RED_WALL,
                new CuboidRegion(ArenaLocation.fromString(data.getString("locations.walls.teamRedWall.min")),
                        ArenaLocation.fromString(data.getString("locations.walls.teamRedWall.max"))));

        regions.put(ArenaRegionType.BLUE_WALL,
                new CuboidRegion(ArenaLocation.fromString(data.getString("locations.walls.teamBlueWall.min")),
                        ArenaLocation.fromString(data.getString("locations.walls.teamBlueWall.max"))));

        regions.put(ArenaRegionType.CENTER,
                new CuboidRegion(ArenaLocation.fromString(data.getString("locations.center.min")),
                        ArenaLocation.fromString(data.getString("locations.center.max"))));

        return new ArenaMetadata(ID, name, locations, regions);
    }

    public PlayableArena toPlayableArena(World world) {
        return new PlayableArena(this, world);
    }

}

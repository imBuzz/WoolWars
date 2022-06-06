package me.buzz.woolwars.game.game.arena;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.buzz.woolwars.api.game.arena.ArenaLocationType;
import me.buzz.woolwars.api.game.arena.region.ArenaRegionType;
import me.buzz.woolwars.api.game.match.type.MatchType;
import me.buzz.woolwars.game.game.arena.location.SerializedLocation;
import me.buzz.woolwars.game.game.arena.regions.CuboidRegion;
import me.buzz.woolwars.game.game.arena.regions.ImplementedRegion;
import me.buzz.woolwars.game.game.arena.settings.preset.ApplicablePreset;
import me.buzz.woolwars.game.game.arena.settings.preset.PresetType;
import me.buzz.woolwars.game.game.arena.settings.preset.impl.ChatPreset;
import me.buzz.woolwars.game.game.arena.settings.preset.impl.ScoreboardPreset;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ArenaMetadata {

    protected final String name;

    @Getter
    protected final String ID;
    @Getter
    protected final MatchType matchType;
    @Getter
    protected final List<SerializedLocation> powerups;

    protected final Map<ArenaLocationType, SerializedLocation> locations;
    protected final Map<ArenaRegionType, ImplementedRegion> regions;
    protected final Map<PresetType, ApplicablePreset<?, ?, ?, ?>> presets;
    @Getter
    private final String worldName;

    public static ArenaMetadata fromFile(File file) {
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);

        String ID = FilenameUtils.removeExtension(file.getName());
        String name = data.getString("name");
        MatchType matchType = MatchType.valueOf(data.getString("type"));
        String worldName = data.getString("worldName");

        Map<ArenaLocationType, SerializedLocation> locations = new HashMap<>();
        locations.put(ArenaLocationType.WAITING_LOBBY, SerializedLocation.fromString(data.getString("locations.spawns.waitingLobby")));
        locations.put(ArenaLocationType.SPAWN_RED, SerializedLocation.fromString(data.getString("locations.spawns.teamRed")));
        locations.put(ArenaLocationType.SPAWN_BLUE, SerializedLocation.fromString(data.getString("locations.spawns.teamBlue")));
        locations.put(ArenaLocationType.NPC_BLUE, SerializedLocation.fromString(data.getString("locations.npc.blue")));
        locations.put(ArenaLocationType.NPC_RED, SerializedLocation.fromString(data.getString("locations.npc.red")));

        Map<ArenaRegionType, ImplementedRegion> regions = new HashMap<>();
        regions.put(ArenaRegionType.RED_WALL, new CuboidRegion(SerializedLocation.fromString(data.getString("locations.walls.teamRedWall.pos1")), SerializedLocation.fromString(data.getString("locations.walls.teamRedWall.pos2"))));
        regions.put(ArenaRegionType.BLUE_WALL, new CuboidRegion(SerializedLocation.fromString(data.getString("locations.walls.teamBlueWall.pos1")), SerializedLocation.fromString(data.getString("locations.walls.teamBlueWall.pos2"))));
        regions.put(ArenaRegionType.CENTER, new CuboidRegion(SerializedLocation.fromString(data.getString("locations.center.pos1")), SerializedLocation.fromString(data.getString("locations.center.pos2"))));

        List<SerializedLocation> powerups = new ArrayList<>();
        for (String serializedLoc : data.getStringList("locations.powerups"))
            powerups.add(SerializedLocation.fromString(serializedLoc));

        Map<PresetType, ApplicablePreset<?, ?, ?, ?>> presets = new HashMap<>();
        presets.put(PresetType.SCOREBOARD, new ScoreboardPreset(data));
        presets.put(PresetType.CHAT, new ChatPreset(data));

        return new ArenaMetadata(ID, name, matchType, powerups, locations, regions, presets, worldName);
    }

    public SerializedLocation getArenaLocation(ArenaLocationType type) {
        return locations.get(type);
    }

    public PlayableArena toPlayableArena(World world) {
        return new PlayableArena(this, world);
    }

    public ApplicablePreset<?, ?, ?, ?> getPreset(PresetType type) {
        return presets.get(type);
    }

}

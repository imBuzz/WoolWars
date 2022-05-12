package me.buzz.woolwars.game.game.arena;

import me.buzz.woolwars.game.game.arena.arena.ArenaMetadata;
import me.buzz.woolwars.game.manager.AbstractManager;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ArenaManager extends AbstractManager {

    private final Map<String, ArenaMetadata> loadedArenas = new HashMap<>();

    @Override
    public void init() {
        File arenasFolder = new File(plugin.getDataFolder().getPath() + File.separator + "arenas");
        if (!arenasFolder.exists()) {
            arenasFolder.mkdirs();
            plugin.getLogger().severe("Cannot load arenas for this session, restart the plugin or fix this problem");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }

        File[] files = arenasFolder.listFiles();
        if (files == null) {
            plugin.getLogger().severe("Cannot load arenas for this session, restart the plugin or fix this problem");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }

        int loadedArenasCounter = 0;

        for (File file : files) {
            ArenaMetadata arenaMetadata = ArenaMetadata.fromFile(file);
            loadedArenas.put(arenaMetadata.getID(), arenaMetadata);
            loadedArenasCounter++;
        }

        plugin.getLogger().info("Loaded " + loadedArenasCounter + " arenas from files");
    }

    @Override
    public void stop() {

    }


}

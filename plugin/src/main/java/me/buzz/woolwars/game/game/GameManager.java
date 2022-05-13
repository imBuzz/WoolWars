package me.buzz.woolwars.game.game;

import me.buzz.woolwars.api.game.ApiGameManager;
import me.buzz.woolwars.api.game.match.ApiMatch;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.game.arena.ArenaMetadata;
import me.buzz.woolwars.game.game.listener.GameListener;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.type.BasicWoolMatch;
import me.buzz.woolwars.game.manager.AbstractManager;
import me.buzz.woolwars.game.player.WoolPlayer;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class GameManager extends AbstractManager implements ApiGameManager {

    private final Map<String, String> matchedIDbyWorldName = new HashMap<>();
    private final Map<String, WoolMatch> matchesByID = new HashMap<>();

    @Override
    public void init() {
        Bukkit.getPluginManager().registerEvents(new GameListener(), WoolWars.get());
        loadGames();
    }

    @Override
    public void stop() {

    }

    private void loadGames() {
        File arenasFolder = new File(plugin.getDataFolder().getPath() + File.separator + "arenas");
        if (!arenasFolder.exists()) {
            arenasFolder.mkdirs();
            plugin.getLogger().severe("Cannot load arenas for this matches, restart the plugin or fix this problem");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }

        File[] files = arenasFolder.listFiles();
        if (files == null) {
            plugin.getLogger().severe("Cannot load arenas for this matches, restart the plugin or fix this problem");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }

        int matchesCounter = 0;

        for (File file : files) {
            ArenaMetadata arenaMetadata = ArenaMetadata.fromFile(file);

            WorldCreator worldCreator = new WorldCreator(arenaMetadata.getWorldName());
            WoolMatch woolMatch = new BasicWoolMatch(arenaMetadata.toPlayableArena(worldCreator.createWorld()));
            woolMatch.init();

            matchesByID.put(woolMatch.getMatchID(), woolMatch);
            matchedIDbyWorldName.put(arenaMetadata.getWorldName(), woolMatch.getMatchID());

            matchesCounter++;
        }

        plugin.getLogger().info("Loaded " + matchesCounter + " matches from files");
    }

    public boolean sendToFreeGame(WoolPlayer woolPlayer) {
        for (WoolMatch value : matchesByID.values()) {
            if (value.checkJoin(woolPlayer)) {
                value.join(woolPlayer);
                return true;
            }
        }
        return false;
    }

    public WoolMatch getInternalMatch(String ID) {
        return matchesByID.get(ID);
    }

    @Override
    public Collection<ApiMatch> getMatchesByID() {
        return Collections.unmodifiableCollection(matchesByID.values());
    }

    public WoolMatch getMatchByWorldName(String worldName) {
        return getInternalMatch(matchedIDbyWorldName.get(worldName));
    }

    @Override
    public Optional<ApiMatch> getMatch(String ID) {
        return Optional.ofNullable(getInternalMatch(ID));
    }

    @Override
    public Optional<ApiMatch> getMatchByPlayer(Player player) {
        return Optional.ofNullable(getInternalMatchByPlayer(player));
    }

    public WoolMatch getInternalMatchByPlayer(Player player) {
        if (player.hasMetadata("wl-playing-game")) {
            String gameID = player.getMetadata("wl-playing-game").get(0).asString();
            return getInternalMatch(gameID);
        } else {
            return null;
        }
    }

}

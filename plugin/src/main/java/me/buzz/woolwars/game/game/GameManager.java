package me.buzz.woolwars.game.game;

import me.buzz.woolwars.api.game.ApiGameManager;
import me.buzz.woolwars.api.game.match.ApiMatch;
import me.buzz.woolwars.api.game.match.player.player.ApiWoolPlayer;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.game.arena.ArenaMetadata;
import me.buzz.woolwars.game.game.listener.GameListener;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.type.BasicWoolMatch;
import me.buzz.woolwars.game.manager.AbstractManager;
import me.buzz.woolwars.game.player.WoolPlayer;
import me.buzz.woolwars.game.utils.workload.WorkloadHandler;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class GameManager extends AbstractManager implements ApiGameManager {

    private final Map<String, String> matchesIDbyWorldName = new HashMap<>();
    private final Map<String, WoolMatch> matchesByID = new HashMap<>();

    @Override
    public void init() {
        Bukkit.getPluginManager().registerEvents(new GameListener(), WoolWars.get());
        loadGames();

        Bukkit.getScheduler().runTaskTimer(WoolWars.get(), () -> WoolMatch.workloadObjects.forEach(WorkloadHandler::addLoad), 1L, 1L);
    }

    @Override
    public void stop() {
        for (WoolMatch value : matchesByID.values()) {
            value.getRoundHolder().reset();
            //value.getPlayerHolder().forWoolPlayers(woolPlayer -> WoolWars.get().getDataProvider().savePlayer(WoolPlayer.removePlayer(woolPlayer.toBukkitPlayer())));
            Bukkit.unloadWorld(value.getPlayableArena().getWorld(), true);
        }

        matchesByID.clear();
        matchesIDbyWorldName.clear();
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

        for (File file : files) {
            ArenaMetadata arenaMetadata = ArenaMetadata.fromFile(file);
            WorldCreator worldCreator = new WorldCreator(arenaMetadata.getWorldName());

            WoolMatch woolMatch = new BasicWoolMatch(arenaMetadata.toPlayableArena(worldCreator.createWorld()));
            woolMatch.init();

            matchesByID.put(woolMatch.getMatchID(), woolMatch);
            matchesIDbyWorldName.put(arenaMetadata.getWorldName(), woolMatch.getMatchID());
        }

        plugin.getLogger().info("Loaded " + matchesByID.size() + " matches from files");
    }

    public boolean sendToFreeGame(WoolPlayer woolPlayer) {
        List<WoolMatch> matches = matchesByID.values().stream().sorted(((o1, o2) -> Integer.compare(o2.getPlayerHolder().getPlayersCount(),
                o1.getPlayerHolder().getPlayersCount()))).collect(Collectors.toList());

        for (WoolMatch value : matches) {
            if (value.checkJoin(woolPlayer)) {
                value.joinAsPlayer(woolPlayer);
                return true;
            } else {
                System.out.println("Cannot join");
            }
        }

        return false;
    }

    public WoolMatch getInternalMatch(String ID) {
        return matchesByID.get(ID);
    }

    @Override
    public Collection<ApiMatch> getMatches() {
        return Collections.unmodifiableCollection(matchesByID.values());
    }

    public WoolMatch getMatchByWorldName(String worldName) {
        return getInternalMatch(matchesIDbyWorldName.get(worldName));
    }

    @Override
    public Optional<ApiMatch> getMatch(String ID) {
        return Optional.ofNullable(getInternalMatch(ID));
    }

    @Override
    public Optional<ApiMatch> getMatchByPlayer(Player player) {
        return Optional.ofNullable(getInternalMatchByPlayer(player));
    }

    @Override
    public ApiWoolPlayer getWoolPlayer(Player player) {
        return WoolPlayer.getWoolPlayer(player);
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

package me.buzz.woolwars.game.game;

import me.buzz.woolwars.api.game.ApiGameManager;
import me.buzz.woolwars.api.game.match.ApiMatch;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.game.listener.GameListener;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.manager.AbstractManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class GameManager extends AbstractManager implements ApiGameManager {

    private final Map<String, WoolMatch> matches = new HashMap<>();

    @Override
    public void init() {
        Bukkit.getPluginManager().registerEvents(new GameListener(), WoolWars.get());
    }

    @Override
    public void stop() {

    }

    public WoolMatch getInternalMatch(String ID) {
        return matches.get(ID);
    }

    @Override
    public Collection<ApiMatch> getMatches() {
        return Collections.unmodifiableCollection(matches.values());
    }

    @Override
    public Optional<ApiMatch> getMatch(String ID) {
        return Optional.ofNullable(getInternalMatch(ID));
    }

    @Override
    public Optional<ApiMatch> getMatchByPlayer(Player player) {
        if (player.hasMetadata("wl-playing-game")) {
            String gameID = player.getMetadata("wl-playing-game").get(0).asString();
            return getMatch(gameID);
        } else {
            return Optional.empty();
        }
    }
}

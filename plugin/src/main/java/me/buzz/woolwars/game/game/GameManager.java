package me.buzz.woolwars.game.game;

import me.buzz.woolwars.api.game.ApiGameManager;
import me.buzz.woolwars.api.game.match.ApiMatch;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.manager.AbstractManager;
import org.bukkit.entity.Player;

import java.util.*;

public class GameManager extends AbstractManager implements ApiGameManager {

    private final Map<String, WoolMatch> matches = new HashMap<>();

    @Override
    public void init() {

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
        return Optional.ofNullable(matches.get(ID));
    }

    @Override
    public Optional<ApiMatch> getMatchByPlayer(Player player) {
        return getMatches().stream().filter(match -> match.getPlayerHolder().getPlayers().contains(player)).findAny();
    }
}

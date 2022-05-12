package me.buzz.woolwars.api.game;

import me.buzz.woolwars.api.game.match.ApiMatch;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;

public interface ApiGameManager {

    Collection<ApiMatch> getMatches();

    Optional<ApiMatch> getMatch(String ID);

    Optional<ApiMatch> getMatchByPlayer(Player player);


}

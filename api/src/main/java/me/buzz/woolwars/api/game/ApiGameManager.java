package me.buzz.woolwars.api.game;

import me.buzz.woolwars.api.game.match.ApiMatch;
import me.buzz.woolwars.api.game.match.player.player.ApiWoolPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;

public interface ApiGameManager {

    /*
     * Get all active matches
     * @return active matches
     */
    Collection<ApiMatch> getMatches();

    /*
     * Get a match by his ID
     * @return match
     */
    Optional<ApiMatch> getMatch(String ID);

    /*
     * Get a match where the player is playing
     * @return match
     */
    Optional<ApiMatch> getMatchByPlayer(Player player);

    ApiWoolPlayer getWoolPlayer(Player player);

}

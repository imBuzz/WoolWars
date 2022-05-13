package me.buzz.woolwars.api.game.match.player;

import com.google.common.collect.ImmutableSet;
import me.buzz.woolwars.api.game.match.player.player.ApiMatchStats;
import org.bukkit.entity.Player;

public interface ApiPlayerHolder {

    boolean isSpectator(Player player);

    void setSpectator(Player player);

    void removeSpectator(Player player);

    ApiMatchStats getMatchStats(Player player);

    ApiMatchStats getMatchStats(String playerName);

    ImmutableSet<Player> getPlayers();


}

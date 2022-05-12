package me.buzz.woolwars.api.game.match.player;

import com.google.common.collect.ImmutableSet;
import me.buzz.woolwars.api.game.match.player.player.ApiMatchStats;
import me.buzz.woolwars.api.game.match.player.player.ApiWoolPlayer;
import org.bukkit.entity.Player;

public interface ApiPlayerHolder {

    ApiWoolPlayer getWoolPlayer();

    ApiMatchStats getMatchStats(Player player);

    ApiMatchStats getMatchStats(String playerName);

    ImmutableSet<Player> getPlayers();


}

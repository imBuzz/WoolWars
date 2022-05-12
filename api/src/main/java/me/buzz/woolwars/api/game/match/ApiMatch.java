package me.buzz.woolwars.api.game.match;

import com.google.common.collect.ImmutableSet;
import me.buzz.woolwars.api.game.arena.ApiPlayableArena;
import me.buzz.woolwars.api.game.match.state.MatchState;
import org.bukkit.entity.Player;

public interface ApiMatch {

    String getMatchID();

    MatchState getMatchState();

    ApiPlayableArena getArena();

    ImmutableSet<Player> getOnlinePlayers();

}

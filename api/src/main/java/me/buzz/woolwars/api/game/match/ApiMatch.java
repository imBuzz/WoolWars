package me.buzz.woolwars.api.game.match;

import me.buzz.woolwars.api.game.arena.ApiPlayableArena;
import me.buzz.woolwars.api.game.match.player.ApiPlayerHolder;
import me.buzz.woolwars.api.game.match.state.MatchState;

public interface ApiMatch {

    String getMatchID();

    MatchState getMatchState();

    ApiPlayableArena getArena();

    ApiPlayerHolder getPlayerHolder();

}

package me.buzz.woolwars.game.game.match;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.buzz.woolwars.api.player.QuitGameReason;
import me.buzz.woolwars.game.game.arena.arena.PlayableArena;
import me.buzz.woolwars.game.game.match.player.PlayerHolder;
import me.buzz.woolwars.game.game.match.player.team.color.TeamColor;
import me.buzz.woolwars.game.game.match.player.team.impl.WoolTeam;
import me.buzz.woolwars.game.game.match.state.MatchState;
import me.buzz.woolwars.game.player.WoolPlayer;
import me.buzz.woolwars.game.utils.UUIDUtils;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public abstract class WoolMatch {

    @Getter protected final String ID = UUIDUtils.getNewUUID();
    protected final PlayableArena arena;
    protected PlayerHolder playerHolder;

    protected final Map<TeamColor, WoolTeam> teams = new HashMap<>();

    @Setter protected MatchState matchState = MatchState.WAITING;

    public abstract void init();

    public abstract void join(WoolPlayer woolPlayer);

    public abstract void quit(WoolPlayer woolPlayer, QuitGameReason reason);

    public abstract void prepare();

    public abstract void start();

    public abstract void end();

    public abstract void postEnd();

    protected abstract int getMaxPlayers();

}

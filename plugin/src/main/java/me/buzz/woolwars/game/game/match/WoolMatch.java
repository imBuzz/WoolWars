package me.buzz.woolwars.game.game.match;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.buzz.woolwars.api.game.arena.ApiPlayableArena;
import me.buzz.woolwars.api.game.match.ApiMatch;
import me.buzz.woolwars.api.game.match.events.MatchChangeStateEvent;
import me.buzz.woolwars.api.game.match.player.team.TeamColor;
import me.buzz.woolwars.api.game.match.state.MatchState;
import me.buzz.woolwars.api.player.QuitGameReason;
import me.buzz.woolwars.game.game.arena.PlayableArena;
import me.buzz.woolwars.game.game.match.entities.powerup.EntityPowerup;
import me.buzz.woolwars.game.game.match.entities.powerup.PowerUPType;
import me.buzz.woolwars.game.game.match.listener.MatchListener;
import me.buzz.woolwars.game.game.match.player.PlayerMatchHolder;
import me.buzz.woolwars.game.game.match.player.team.impl.WoolTeam;
import me.buzz.woolwars.game.game.match.round.RoundMatchHolder;
import me.buzz.woolwars.game.player.WoolPlayer;
import me.buzz.woolwars.game.utils.UUIDUtils;
import me.buzz.woolwars.game.utils.workload.Workload;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public abstract class WoolMatch implements ApiMatch {

    public final static List<Workload> workloadObjects = new ArrayList<>();

    protected final String ID = UUIDUtils.getNewUUID();
    protected final PlayableArena arena;

    @Getter
    protected final Map<TeamColor, WoolTeam> teams = new HashMap<>();

    protected MatchState matchState = MatchState.WAITING;
    @Getter
    protected MatchListener matchListener;

    @Getter
    protected PlayerMatchHolder playerHolder;
    @Getter
    protected RoundMatchHolder roundHolder;

    public abstract void init();

    public abstract boolean checkJoin(WoolPlayer woolPlayer);

    public abstract void joinAsPlayer(WoolPlayer woolPlayer);

    public abstract void joinAsSpectator(WoolPlayer woolPlayer);

    public abstract void quit(WoolPlayer woolPlayer, QuitGameReason reason);

    public abstract void cooldown();

    public abstract void prepare();

    public abstract void start();

    public abstract void end(WoolTeam winnerTeam);

    public abstract void reset();

    public abstract void pickUP(Player player, EntityPowerup entity, PowerUPType type);

    public abstract void handleDeath(Player victim, Player killer, EntityDamageEvent.DamageCause cause);

    public abstract int getMaxPlayers();

    public abstract int getPointsToWin();

    public boolean isPlaying() {
        return matchState == MatchState.PRE_ROUND || matchState == MatchState.ROUND || matchState == MatchState.ROUND_OVER;
    }

    public void setMatchState(MatchState matchState) {
        Bukkit.getPluginManager().callEvent(new MatchChangeStateEvent(this, this.matchState, matchState));
        this.matchState = matchState;
    }

    @Override
    public String getMatchID() {
        return ID;
    }

    @Override
    public MatchState getMatchState() {
        return matchState;
    }

    @Override
    public ApiPlayableArena getArena() {
        return getPlayableArena();
    }

    public PlayableArena getPlayableArena() {
        return arena;
    }


}

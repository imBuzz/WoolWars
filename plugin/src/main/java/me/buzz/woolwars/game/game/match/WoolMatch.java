package me.buzz.woolwars.game.game.match;

import ch.jalu.configme.SettingsManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.buzz.woolwars.api.game.arena.ApiPlayableArena;
import me.buzz.woolwars.api.game.match.ApiMatch;
import me.buzz.woolwars.api.game.match.state.MatchState;
import me.buzz.woolwars.api.player.QuitGameReason;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.game.arena.PlayableArena;
import me.buzz.woolwars.game.game.match.listener.MatchListener;
import me.buzz.woolwars.game.game.match.player.PlayerHolder;
import me.buzz.woolwars.game.game.match.player.team.color.TeamColor;
import me.buzz.woolwars.game.game.match.player.team.impl.WoolTeam;
import me.buzz.woolwars.game.game.match.round.RoundHolder;
import me.buzz.woolwars.game.player.WoolPlayer;
import me.buzz.woolwars.game.utils.UUIDUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public abstract class WoolMatch implements ApiMatch {

    @Getter
    protected final SettingsManager language = WoolWars.get().getLanguage();

    protected final String ID = UUIDUtils.getNewUUID();
    @Getter
    protected final Map<TeamColor, WoolTeam> teams = new HashMap<>();
    protected final PlayableArena arena;

    @Setter
    protected MatchState matchState = MatchState.WAITING;
    @Getter
    protected MatchListener matchListener;

    @Getter
    protected PlayerHolder playerHolder;
    @Getter
    protected RoundHolder roundHolder;

    public abstract void init();

    public abstract boolean checkJoin(WoolPlayer woolPlayer);

    public abstract void join(WoolPlayer woolPlayer);

    public abstract void quit(WoolPlayer woolPlayer, QuitGameReason reason);

    public abstract void cooldown();

    public abstract void prepare();

    public abstract void start();

    public abstract void end();

    public abstract void handleDeath(Player victim, Player killer, EntityDamageEvent.DamageCause cause);

    protected abstract int getMaxPlayers();

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

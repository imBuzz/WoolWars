package me.buzz.woolwars.game.game.match;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.buzz.woolwars.api.player.QuitGameReason;
import me.buzz.woolwars.game.game.arena.arena.PlayableArena;
import me.buzz.woolwars.game.game.match.player.team.color.TeamColor;
import me.buzz.woolwars.game.game.match.player.team.impl.WoolTeam;
import me.buzz.woolwars.game.game.match.state.MatchState;
import me.buzz.woolwars.game.player.WoolPlayer;
import me.buzz.woolwars.game.utils.UUIDUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class WoolMatch {

    @Getter protected final String ID = UUIDUtils.getNewUUID();
    protected final PlayableArena arena;

    protected final Map<String, WoolPlayer> players = new HashMap<>();
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

    public List<Player> getOnlinePlayers(){
        return players.values().stream()
                .map(WoolPlayer::toPlayer)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}

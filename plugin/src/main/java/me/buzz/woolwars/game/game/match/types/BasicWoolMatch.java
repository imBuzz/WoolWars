package me.buzz.woolwars.game.game.match.types;

import com.google.common.collect.Lists;
import me.buzz.woolwars.api.events.PlayerJoinGameEvent;
import me.buzz.woolwars.api.events.PlayerQuitGameEvent;
import me.buzz.woolwars.api.player.QuitGameReason;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.game.arena.arena.PlayableArena;
import me.buzz.woolwars.game.game.arena.location.ArenaLocationType;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.player.team.color.TeamColor;
import me.buzz.woolwars.game.game.match.player.team.impl.WoolTeam;
import me.buzz.woolwars.game.game.match.state.MatchState;
import me.buzz.woolwars.game.player.WoolPlayer;
import me.buzz.woolwars.game.utils.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BasicWoolMatch extends WoolMatch {
    private final static int MAX_PLAYERS = 8;
    private final static int MIN_PLAYERS_PER_TEAM = 2;

    public BasicWoolMatch(PlayableArena arena) {
        super(arena);
    }

    @Override
    public void init() {

    }

    @Override
    public void join(WoolPlayer woolPlayer) {
        Player player = woolPlayer.toPlayer();
        if (players.size() >= getMaxPlayers()) {
            player.sendMessage("TOO MANY PLAYERS");
            return;
        }

        PlayerJoinGameEvent joinGameEvent = new PlayerJoinGameEvent(player);
        Bukkit.getPluginManager().callEvent(joinGameEvent);
        if (joinGameEvent.isCancelled()) return;

        player.setMetadata("wl-playing-game", new FixedMetadataValue(WoolWars.get(), ID));
        players.put(player.getName(), woolPlayer);
        player.teleport(arena.getBukkitLocation(ArenaLocationType.WAITING_LOBBY));

        //TODO: JOIN MESSAGE
    }

    @Override
    public void quit(WoolPlayer woolPlayer, QuitGameReason reason) {
        Player player = woolPlayer.toPlayer();

        PlayerQuitGameEvent quitGameEvent = new PlayerQuitGameEvent(player, reason);
        Bukkit.getPluginManager().callEvent(quitGameEvent);

        //TODO: SEND QUIT MESSAGE CHECK SENDMESSAGE ON EVENT

        players.remove(player.getName());
        player.removeMetadata("wl-playing-game", WoolWars.get());

        if (matchState == MatchState.PLAYING) {
            if (woolPlayer.getTeam().getOnlinePlayers().size() - 1 < MIN_PLAYERS_PER_TEAM) {
                //TODO: INSUFFICIENT PLAYERS MESSAGE BY TEAM
                setMatchState(MatchState.ENDING);
            }
        }
    }

    @Override
    public void prepare() {
        teams.put(TeamColor.RED, new WoolTeam(ID, TeamColor.RED, arena.getBukkitLocation(ArenaLocationType.SPAWN_RED)));
        teams.put(TeamColor.BLUE, new WoolTeam(ID, TeamColor.BLUE, arena.getBukkitLocation(ArenaLocationType.SPAWN_BLUE)));

        List<WoolPlayer> p = new ArrayList<>(players.values());
        List<List<WoolPlayer>> groups = Lists.partition(p, TeamUtils.getHalfApprox(p.size()));

        for (WoolPlayer woolPlayer : groups.get(0)) teams.get(TeamColor.RED).join(woolPlayer);
        for (WoolPlayer woolPlayer : groups.get(1)) teams.get(TeamColor.BLUE).join(woolPlayer);

        start();
    }

    @Override
    public void start() {


    }

    @Override
    public void end() {

    }

    @Override
    public void postEnd() {

    }

    @Override
    protected int getMaxPlayers() {
        return MAX_PLAYERS;
    }

    @Override
    public void setMatchState(MatchState state) {
        super.setMatchState(state);
        switch (matchState){
            case STARTING: {
                prepare();
                break;
            }
            case ENDING: {
                end();
                break;
            }
            case ENDED: {
                postEnd();
                break;
            }
            default: break;
        }
    }
}

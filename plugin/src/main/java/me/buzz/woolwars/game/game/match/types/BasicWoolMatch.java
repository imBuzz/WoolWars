package me.buzz.woolwars.game.game.match.types;

import com.google.common.collect.Lists;
import me.buzz.woolwars.api.game.arena.ArenaLocationType;
import me.buzz.woolwars.api.game.arena.ArenaRegionType;
import me.buzz.woolwars.api.game.match.events.PlayerJoinGameEvent;
import me.buzz.woolwars.api.game.match.events.PlayerQuitGameEvent;
import me.buzz.woolwars.api.game.match.state.MatchState;
import me.buzz.woolwars.api.player.QuitGameReason;
import me.buzz.woolwars.game.game.arena.arena.PlayableArena;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.player.PlayerHolder;
import me.buzz.woolwars.game.game.match.player.classes.PlayableClass;
import me.buzz.woolwars.game.game.match.player.classes.classes.BerserkPlayableClass;
import me.buzz.woolwars.game.game.match.player.stats.MatchStats;
import me.buzz.woolwars.game.game.match.player.team.color.TeamColor;
import me.buzz.woolwars.game.game.match.player.team.impl.WoolTeam;
import me.buzz.woolwars.game.player.WoolPlayer;
import me.buzz.woolwars.game.utils.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BasicWoolMatch extends WoolMatch {
    private final static int MAX_PLAYERS = 8;
    private final static int MIN_PLAYERS_PER_TEAM = 1;

    public BasicWoolMatch(PlayableArena arena) {
        super(arena);
    }

    @Override
    public void init() {
        playerHolder = new PlayerHolder(this);
    }

    @Override
    public void join(WoolPlayer woolPlayer) {
        Player player = woolPlayer.toBukkitPlayer();
        if (playerHolder.getPlayersCount() >= getMaxPlayers()) {
            player.sendMessage("TOO MANY PLAYERS");
            return;
        }

        PlayerJoinGameEvent joinGameEvent = new PlayerJoinGameEvent(player);
        Bukkit.getPluginManager().callEvent(joinGameEvent);
        if (joinGameEvent.isCancelled()) return;

        playerHolder.registerPlayer(woolPlayer);
        player.teleport(arena.getLocation(ArenaLocationType.WAITING_LOBBY));

        //TODO: JOIN MESSAGE
    }

    @Override
    public void quit(WoolPlayer woolPlayer, QuitGameReason reason) {
        Player player = woolPlayer.toBukkitPlayer();
        boolean shouldEnd = false;

        PlayerQuitGameEvent quitGameEvent = new PlayerQuitGameEvent(player, reason);
        Bukkit.getPluginManager().callEvent(quitGameEvent);

        if (matchState == MatchState.PLAYING)
            shouldEnd = playerHolder.getMatchStats(player).getTeam().getOnlinePlayers().size() - 1 < MIN_PLAYERS_PER_TEAM;

        //TODO: SEND QUIT MESSAGE CHECK SENDMESSAGE ON EVENT
        playerHolder.removePlayer(woolPlayer);

        if (shouldEnd && !isEnded()) {
            //TODO: INSUFFICIENT PLAYERS MESSAGE BY TEAM
            setMatchState(MatchState.ENDING);
        }
    }

    @Override
    public void prepare() {
        teams.put(TeamColor.RED, new WoolTeam(ID, TeamColor.RED, arena.getLocation(ArenaLocationType.SPAWN_RED)));
        teams.put(TeamColor.BLUE, new WoolTeam(ID, TeamColor.BLUE, arena.getLocation(ArenaLocationType.SPAWN_BLUE)));

        List<WoolPlayer> p = new ArrayList<>(playerHolder.getWoolPlayers());
        List<List<WoolPlayer>> groups = Lists.partition(p, TeamUtils.getHalfApprox(p.size()));

        for (WoolPlayer woolPlayer : groups.get(0)) teams.get(TeamColor.RED).join(woolPlayer);
        for (WoolPlayer woolPlayer : groups.get(1)) teams.get(TeamColor.BLUE).join(woolPlayer);

        preStart();
    }

    @Override
    public void preStart() {
        for (WoolPlayer woolPlayer : playerHolder.getWoolPlayers()) {
            Player player = woolPlayer.toBukkitPlayer();
            MatchStats matchStats = playerHolder.getMatchStats(player);

            PlayableClass selectedClass = matchStats.getPlayableClass();
            if (selectedClass == null)
                selectedClass = new BerserkPlayableClass(player, matchStats.getTeam().getTeamColor());

            selectedClass.equip();
            player.teleport(matchStats.getTeam().getSpawnLocation());
        }

        //TODO: START NEW COOLDOWN TASK
    }

    @Override
    public void start() {
        for (Block block : arena.getRegion(ArenaRegionType.RED_WALL).getBlocks()) {
            block.setType(Material.AIR);
        }
        for (Block block : arena.getRegion(ArenaRegionType.BLUE_WALL).getBlocks()) {
            block.setType(Material.AIR);
        }

        //TODO: MESSAGES
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
            default:
                break;
        }
    }

    private boolean isEnded() {
        return matchState == MatchState.ENDED || matchState == MatchState.ENDING;
    }
}

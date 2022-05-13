package me.buzz.woolwars.game.game.match.type;

import com.google.common.collect.Lists;
import me.buzz.woolwars.api.game.arena.ArenaLocationType;
import me.buzz.woolwars.api.game.match.events.PlayerJoinGameEvent;
import me.buzz.woolwars.api.game.match.events.PlayerQuitGameEvent;
import me.buzz.woolwars.api.game.match.state.MatchState;
import me.buzz.woolwars.api.player.QuitGameReason;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.game.arena.arena.PlayableArena;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.listener.impl.BasicMatchListener;
import me.buzz.woolwars.game.game.match.player.PlayerHolder;
import me.buzz.woolwars.game.game.match.player.stats.MatchStats;
import me.buzz.woolwars.game.game.match.player.team.color.TeamColor;
import me.buzz.woolwars.game.game.match.player.team.impl.WoolTeam;
import me.buzz.woolwars.game.game.match.round.RoundHolder;
import me.buzz.woolwars.game.game.match.task.CooldownTask;
import me.buzz.woolwars.game.player.WoolPlayer;
import me.buzz.woolwars.game.utils.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
        roundHolder = new RoundHolder(this);
        matchListener = new BasicMatchListener(this);
    }

    @Override
    public boolean checkJoin(WoolPlayer woolPlayer) {
        return playerHolder.getPlayersCount() + 1 > getMaxPlayers();
    }

    @Override
    public void join(WoolPlayer woolPlayer) {
        Player player = woolPlayer.toBukkitPlayer();

        PlayerJoinGameEvent joinGameEvent = new PlayerJoinGameEvent(player);
        Bukkit.getPluginManager().callEvent(joinGameEvent);
        if (joinGameEvent.isCancelled()) return;

        playerHolder.registerPlayer(woolPlayer);
        player.teleport(arena.getLocation(ArenaLocationType.WAITING_LOBBY));

        //TODO: JOIN MESSAGE

        if (playerHolder.getPlayersCount() == getMaxPlayers()) {
            setMatchState(MatchState.COOLDOWN);
        }
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
    public void cooldown() {
        new CooldownTask(this, () -> setMatchState(MatchState.STARTING), 5).runTaskTimer(WoolWars.get(), 0L, 20L);
    }

    @Override
    public void prepare() {
        teams.put(TeamColor.RED, new WoolTeam(ID, TeamColor.RED, arena.getLocation(ArenaLocationType.SPAWN_RED)));
        teams.put(TeamColor.BLUE, new WoolTeam(ID, TeamColor.BLUE, arena.getLocation(ArenaLocationType.SPAWN_BLUE)));

        List<WoolPlayer> p = new ArrayList<>(playerHolder.getWoolPlayers());
        List<List<WoolPlayer>> groups = Lists.partition(p, TeamUtils.getHalfApprox(p.size()));

        for (WoolPlayer woolPlayer : groups.get(0)) teams.get(TeamColor.RED).join(woolPlayer);
        for (WoolPlayer woolPlayer : groups.get(1)) teams.get(TeamColor.BLUE).join(woolPlayer);

        start();
    }

    @Override
    public void start() {
        roundHolder.startNewRound();
    }

    @Override
    public void end() {
        for (Player onlinePlayer : playerHolder.getOnlinePlayers()) {
            onlinePlayer.sendMessage("Ended");
        }
    }

    @Override
    public void handleDeath(Player victim, Player killer, EntityDamageEvent.DamageCause cause) {
        MatchStats victimStats = playerHolder.getMatchStats(victim);
        victimStats.matchDeaths++;
        if (killer != null) playerHolder.getMatchStats(killer).matchKills++;

        victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 1));
        victim.sendTitle("SEI MORTO", "COGLIONE!");
        playerHolder.setSpectator(victim);

        if (cause == EntityDamageEvent.DamageCause.VOID) {
            victim.teleport(victimStats.getTeam().getSpawnLocation());
        }
    }

    @Override
    protected int getMaxPlayers() {
        return MAX_PLAYERS;
    }

    @Override
    public void setMatchState(MatchState state) {
        super.setMatchState(state);
        switch (matchState) {
            case COOLDOWN: {
                cooldown();
                break;
            }
            case STARTING: {
                prepare();
                break;
            }
            case ENDING: {
                end();
                break;
            }
            default:
                break;
        }
    }

    private boolean isEnded() {
        return matchState == MatchState.ENDING;
    }
}

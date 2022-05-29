package me.buzz.woolwars.game.game.match.type;

import com.google.common.collect.Lists;
import com.hakan.core.HCore;
import com.hakan.core.npc.HNPC;
import me.buzz.woolwars.api.game.arena.ArenaLocationType;
import me.buzz.woolwars.api.game.match.player.events.PlayerDeathByPlayerEvent;
import me.buzz.woolwars.api.game.match.player.events.PlayerDeathEvent;
import me.buzz.woolwars.api.game.match.player.events.PlayerJoinGameEvent;
import me.buzz.woolwars.api.game.match.player.events.PlayerQuitGameEvent;
import me.buzz.woolwars.api.game.match.state.MatchState;
import me.buzz.woolwars.api.player.QuitGameReason;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.ConfigFile;
import me.buzz.woolwars.game.configuration.files.lang.LanguageFile;
import me.buzz.woolwars.game.game.arena.PlayableArena;
import me.buzz.woolwars.game.game.gui.ClassSelectorGui;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.entities.powerup.EntityPowerup;
import me.buzz.woolwars.game.game.match.entities.powerup.PowerUPType;
import me.buzz.woolwars.game.game.match.listener.impl.BasicMatchListener;
import me.buzz.woolwars.game.game.match.player.PlayerHolder;
import me.buzz.woolwars.game.game.match.player.stats.MatchStats;
import me.buzz.woolwars.game.game.match.player.team.color.TeamColor;
import me.buzz.woolwars.game.game.match.player.team.impl.WoolTeam;
import me.buzz.woolwars.game.game.match.round.RoundHolder;
import me.buzz.woolwars.game.game.match.task.tasks.ResetMatchTask;
import me.buzz.woolwars.game.game.match.task.tasks.StartingMatchTask;
import me.buzz.woolwars.game.player.WoolPlayer;
import me.buzz.woolwars.game.utils.StringsUtils;
import me.buzz.woolwars.game.utils.TeamUtils;
import me.buzz.woolwars.game.utils.UUIDUtils;
import me.buzz.woolwars.game.utils.structures.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
        return playerHolder.getPlayersCount() + 1 <= getMaxPlayers() && matchState == MatchState.WAITING;
    }

    @Override
    public void join(WoolPlayer woolPlayer) {
        Player player = woolPlayer.toBukkitPlayer();

        PlayerJoinGameEvent joinGameEvent = new PlayerJoinGameEvent(player);
        Bukkit.getPluginManager().callEvent(joinGameEvent);
        if (joinGameEvent.isCancelled()) return;

        playerHolder.registerPlayer(woolPlayer);
        player.teleport(arena.getLocation(ArenaLocationType.WAITING_LOBBY));

        for (Player matchPlayer : playerHolder.getOnlinePlayers()) {
            matchPlayer.sendMessage(WoolWars.get().getLanguage().getProperty(LanguageFile.JOINED_MESSAGE)
                    .replace("{player}", player.getName())
                    .replace("{current}", String.valueOf(playerHolder.getPlayersCount()))
                    .replace("{max}", String.valueOf(getMaxPlayers())));
        }

        if (playerHolder.getPlayersCount() >= getMaxPlayers()) {
            setMatchState(MatchState.STARTING);
        }
    }

    @Override
    public void quit(WoolPlayer woolPlayer, QuitGameReason reason) {
        Player player = woolPlayer.toBukkitPlayer();
        boolean shouldEnd = false;

        PlayerQuitGameEvent quitGameEvent = new PlayerQuitGameEvent(player, reason);
        Bukkit.getPluginManager().callEvent(quitGameEvent);

        MatchStats matchStats = playerHolder.getMatchStats(player);

        if (isPlaying())
            shouldEnd = matchStats.getTeam().getOnlinePlayers().size() - 1 < MIN_PLAYERS_PER_TEAM;

        if (quitGameEvent.isSendMessage()) {
            for (Player matchPlayer : playerHolder.getOnlinePlayers()) {
                matchPlayer.sendMessage(WoolWars.get().getLanguage().getProperty(LanguageFile.LEAVE_MESSAGE)
                        .replace("{player}", player.getName())
                        .replace("{current}", String.valueOf(playerHolder.getPlayersCount() - 1))
                        .replace("{max}", String.valueOf(getMaxPlayers())));
            }
        }

        if (matchStats.getTeam() != null) matchStats.getTeam().remove(player);

        woolPlayer.transferFrom(matchStats, false);
        playerHolder.removePlayer(woolPlayer);

        if (shouldEnd && !isEnded()) {
            for (Player matchPlayer : playerHolder.getOnlinePlayers())
                matchPlayer.sendMessage(WoolWars.get().getLanguage().getProperty(LanguageFile.NOT_ENOUGH_PLAYER_TO_PLAY));
            end(null);
        }
    }

    @Override
    public void cooldown() {
        roundHolder.getTasks().put(StartingMatchTask.ID, new StartingMatchTask(this,
                TimeUnit.SECONDS.toMillis(WoolWars.get().getSettings().getProperty(ConfigFile.START_COOLDOWN))).start());
    }

    @Override
    public void prepare() {
        List<WoolPlayer> p = new ArrayList<>(playerHolder.getWoolPlayers());
        List<List<WoolPlayer>> groups = Lists.partition(p, TeamUtils.getHalfApprox(p.size()));

        for (int i = 0; i < TeamColor.values().length; i++) {
            TeamColor teamColor = TeamColor.values()[i];
            WoolTeam team = new WoolTeam(ID, teamColor, arena.getLocation(teamColor == TeamColor.RED ? ArenaLocationType.SPAWN_RED : ArenaLocationType.SPAWN_BLUE));

            HNPC npc = HCore.buildNpc(UUIDUtils.getNewUUID())
                    .showEveryone(false)
                    .location(WoolWars.get().getSettings().getProperty(teamColor == TeamColor.RED ? ConfigFile.NPC_LOCATION_RED :
                            ConfigFile.NPC_LOCATION_BLUE).toBukkitLocation(arena.getWorld()))
                    .lines(WoolWars.get().getLanguage().getProperty(LanguageFile.NPC_NAME))
                    .skin(WoolWars.get().getLanguage().getProperty(LanguageFile.NPC_SKIN))
                    .action((player -> new ClassSelectorGui(this, playerHolder.getMatchStats(player)).open(player)))
                    .build();

            team.setTeamNPC(npc);

            for (WoolPlayer woolPlayer : groups.get(i))
                team.join(woolPlayer, playerHolder.getMatchStats(woolPlayer.getName()));

            teams.put(teamColor, team);
        }

        start();
    }

    @Override
    public void start() {
        roundHolder.startNewRound();
    }

    @Override
    public void end(WoolTeam winnerTeam) {
        setMatchState(MatchState.ENDING);

        Map<String, Integer> topKillers = TeamUtils.getTopKillers(this);
        Map<String, Integer> topWool = TeamUtils.getTopWool(this);
        Map<String, Integer> topBlocks = TeamUtils.getTopBroken(this);

        Integer[] topKillers_value = topKillers.values().toArray(new Integer[0]);
        String[] topKillers_names = topKillers.keySet().toArray(new String[0]);

        Integer[] topWool_value = topWool.values().toArray(new Integer[0]);
        String[] topWool_names = topWool.keySet().toArray(new String[0]);

        Integer[] topBlocks_value = topBlocks.values().toArray(new Integer[0]);
        String[] topBlocks_names = topBlocks.keySet().toArray(new String[0]);

        List<String> tempLines = WoolWars.get().getLanguage().getProperty(LanguageFile.ENDED_RESUME);

        for (WoolPlayer woolPlayer : playerHolder.getWoolPlayers()) {
            Player player = woolPlayer.toBukkitPlayer();

            MatchStats stats = playerHolder.getMatchStats(player);

            boolean isWinnerTeam = winnerTeam != null && stats.getTeam() == winnerTeam;

            woolPlayer.transferFrom(stats, isWinnerTeam);
            playerHolder.setSpectator(player);

            List<String> lines = new ArrayList<>();
            for (String tempLine : tempLines) {
                lines.add(tempLine
                        .replace("{status}", WoolWars.get().getLanguage().getProperty(isWinnerTeam ? LanguageFile.ENDED_STATUS_VICTORY : LanguageFile.ENDED_STATUS_LOST))

                        .replace("{top_killer_name}", topKillers_names[0])
                        .replace("{top_kills}", String.valueOf(topKillers_value[0]))

                        .replace("{top_wool_name}", topWool_names[0])
                        .replace("{top_wool}", String.valueOf(topWool_value[0]))

                        .replace("{top_blocks_name}", topBlocks_names[0])
                        .replace("{top_blocks}", String.valueOf(topBlocks_value[0]))
                );
            }

            Title title = WoolWars.get().getLanguage().getProperty(isWinnerTeam ? LanguageFile.ENDED_VICTORY_TITLE : LanguageFile.ENDED_LOST_TITLE);
            player.sendTitle(title.getTitle(), title.getSubTitle());

            if (WoolWars.get().getLanguage().getProperty(LanguageFile.ENDED_RESUME_CENTERED)) {
                for (String line : lines) {
                    if (line.isEmpty()) player.sendMessage(line);
                    else StringsUtils.sendCenteredMessage(player, line);
                }
            } else {
                for (String line : lines) {
                    player.sendMessage(line);
                }
            }
        }

        roundHolder.getTasks().put(ResetMatchTask.ID, new ResetMatchTask(this,
                TimeUnit.SECONDS.toMillis(WoolWars.get().getSettings().getProperty(ConfigFile.CLOSE_GAME_COOLDOWN))).start());
    }

    @Override
    public void reset() {
        for (WoolTeam value : teams.values()) value.getTeamNPC().delete();
        teams.clear();
        roundHolder.reset();
        playerHolder.reset();
        matchState = MatchState.WAITING;
    }

    @Override
    public void pickUP(Player player, EntityPowerup entity, PowerUPType type) {
        playerHolder.getMatchStats(player).matchPowerUpsGotten++;
        player.sendMessage(WoolWars.get().getLanguage().getProperty(type.getProperty()).getPickupMessage());
        roundHolder.getEntities().remove(entity);
    }

    @Override
    public void handleDeath(Player victim, Player killer, EntityDamageEvent.DamageCause cause) {
        MatchStats victimStats = playerHolder.getMatchStats(victim);
        if (!playerHolder.isSpectator(victim)) {
            playerHolder.setSpectator(victim);

            victimStats.matchDeaths++;
            if (killer != null) playerHolder.getMatchStats(killer).matchKills++;

            victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 2, 1));

            Title title = WoolWars.get().getLanguage().getProperty(LanguageFile.DIED_TITLE);
            victim.sendTitle(title.getTitle(), title.getSubTitle());
        }

        if (killer != null) Bukkit.getPluginManager().callEvent(new PlayerDeathByPlayerEvent(killer, victim));
        else Bukkit.getPluginManager().callEvent(new PlayerDeathEvent(victim));

        if (cause == EntityDamageEvent.DamageCause.VOID) {
            victim.teleport(victimStats.getTeam().getSpawnLocation());
        }
    }

    @Override
    public int getMaxPlayers() {
        return MAX_PLAYERS;
    }

    @Override
    public int getPointsToWin() {
        return 3;
    }

    @Override
    public void setMatchState(MatchState state) {
        super.setMatchState(state);
        if (matchState == MatchState.STARTING) {
            cooldown();
        }
    }

    private boolean isEnded() {
        return matchState == MatchState.ENDING;
    }
}

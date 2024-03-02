package me.buzz.woolwars.game.game.match.type;

import ch.jalu.configme.properties.Property;
import com.google.common.collect.Lists;
import com.hakan.core.HCore;
import com.hakan.core.npc.Npc;
import com.hakan.core.skin.Skin;
import me.buzz.woolwars.api.game.arena.ArenaLocationType;
import me.buzz.woolwars.api.game.arena.region.ArenaRegionType;
import me.buzz.woolwars.api.game.arena.region.Region;
import me.buzz.woolwars.api.game.match.player.events.PlayerDeathByPlayerEvent;
import me.buzz.woolwars.api.game.match.player.events.PlayerDeathEvent;
import me.buzz.woolwars.api.game.match.player.events.PlayerJoinGameEvent;
import me.buzz.woolwars.api.game.match.player.events.PlayerQuitGameEvent;
import me.buzz.woolwars.api.game.match.player.team.TeamColor;
import me.buzz.woolwars.api.game.match.state.MatchState;
import me.buzz.woolwars.api.player.QuitGameReason;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.ConfigFile;
import me.buzz.woolwars.game.configuration.files.lang.LanguageFile;
import me.buzz.woolwars.game.game.arena.PlayableArena;
import me.buzz.woolwars.game.game.arena.settings.preset.ApplicablePreset;
import me.buzz.woolwars.game.game.arena.settings.preset.PresetType;
import me.buzz.woolwars.game.game.arena.settings.preset.impl.ChatPreset;
import me.buzz.woolwars.game.game.gui.ClassSelectorGui;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.entities.powerup.EntityPowerup;
import me.buzz.woolwars.game.game.match.entities.powerup.PowerUPType;
import me.buzz.woolwars.game.game.match.listener.impl.BasicMatchListener;
import me.buzz.woolwars.game.game.match.player.PlayerMatchHolder;
import me.buzz.woolwars.game.game.match.player.stats.WoolMatchStats;
import me.buzz.woolwars.game.game.match.player.team.impl.WoolTeam;
import me.buzz.woolwars.game.game.match.round.RoundMatchHolder;
import me.buzz.woolwars.game.game.match.task.tasks.ResetMatchTask;
import me.buzz.woolwars.game.game.match.task.tasks.StartingMatchTask;
import me.buzz.woolwars.game.player.WoolPlayer;
import me.buzz.woolwars.game.utils.StringsUtils;
import me.buzz.woolwars.game.utils.TeamUtils;
import me.buzz.woolwars.game.utils.UUIDUtils;
import me.buzz.woolwars.game.utils.structures.Title;
import me.buzz.woolwars.game.utils.structures.WoolItem;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class BasicWoolMatch extends WoolMatch {
    private final static int MIN_PLAYERS_PER_TEAM = 1;

    public BasicWoolMatch(PlayableArena arena) {
        super(arena);
    }

    @Override
    public void init() {
        playerHolder = new PlayerMatchHolder(this);
        roundHolder = new RoundMatchHolder(this);
        matchListener = new BasicMatchListener(this);
    }

    @Override
    public boolean checkJoin(WoolPlayer woolPlayer) {
        return playerHolder.getPlayersCount() + 1 <= getMaxPlayers() && matchState == MatchState.WAITING;
    }

    @Override
    public void joinAsPlayer(WoolPlayer woolPlayer) {
        Player player = woolPlayer.toBukkitPlayer();

        PlayerJoinGameEvent joinGameEvent = new PlayerJoinGameEvent(player);
        Bukkit.getPluginManager().callEvent(joinGameEvent);
        if (joinGameEvent.isCancelled()) return;

        playerHolder.registerPlayer(woolPlayer, true);

        Set<Player> playerSet = playerHolder.getOnlinePlayers();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer == player) continue;

            if (playerSet.contains(onlinePlayer)) {
                if (playerHolder.isSpectator(onlinePlayer)) {
                    if (playerHolder.isSpectator(player)) {
                        player.showPlayer(onlinePlayer);
                        onlinePlayer.showPlayer(player);
                    } else {
                        player.hidePlayer(onlinePlayer);
                    }
                } else {
                    player.showPlayer(onlinePlayer);
                    onlinePlayer.showPlayer(player);
                }
            } else {
                onlinePlayer.hidePlayer(player);
                player.hidePlayer(onlinePlayer);
            }
        }

        WoolItem lobbyItem = WoolWars.get().getLanguage().getProperty(LanguageFile.RETURN_TO_LOBBY);
        player.getInventory().setItem(lobbyItem.getSlot(), lobbyItem.toItemStack());
        player.teleport(arena.getLocation(ArenaLocationType.WAITING_LOBBY));
        WoolWars.get().getSettings().getProperty(ConfigFile.SOUNDS_TELEPORT).play(player, 1, 1);

        if (joinGameEvent.isSendMessage()) {
            String leaveMessage = ((ApplicablePreset<String, WoolMatch, Player, ChatPreset.AskingChatMotivation>) arena.getPreset(PresetType.CHAT))
                    .apply(this, player, ChatPreset.AskingChatMotivation.JOIN);

            for (Player matchPlayer : playerHolder.getOnlinePlayers()) {
                matchPlayer.sendMessage(leaveMessage);
                WoolWars.get().getSettings().getProperty(ConfigFile.SOUNDS_PLAYER_JOINED).play(matchPlayer, 1, 1);
            }
        }
        if (playerHolder.getPlayersCount() >= getMaxPlayers()) {
            setMatchState(MatchState.STARTING);
        }
    }

    @Override
    public void joinAsSpectator(WoolPlayer woolPlayer) {
        Player player = woolPlayer.toBukkitPlayer();

        playerHolder.registerPlayer(woolPlayer, false);
        playerHolder.setSpectator(player, true);

        WoolItem lobbyItem = WoolWars.get().getLanguage().getProperty(LanguageFile.RETURN_TO_LOBBY);
        player.getInventory().setItem(lobbyItem.getSlot(), lobbyItem.toItemStack());

        Set<Player> playerSet = playerHolder.getOnlinePlayers();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer == player) continue;

            if (playerSet.contains(onlinePlayer)) {
                if (playerHolder.isSpectator(onlinePlayer)) {
                    if (playerHolder.isSpectator(player)) {
                        player.showPlayer(onlinePlayer);
                        onlinePlayer.showPlayer(player);
                    } else {
                        player.hidePlayer(onlinePlayer);
                    }
                } else {
                    player.showPlayer(onlinePlayer);
                    onlinePlayer.showPlayer(player);
                }
            } else {
                onlinePlayer.hidePlayer(player);
                player.hidePlayer(onlinePlayer);
            }
        }
    }

    @Override
    public void quit(WoolPlayer woolPlayer, QuitGameReason reason) {
        Player player = woolPlayer.toBukkitPlayer();
        boolean shouldEnd = false;

        PlayerQuitGameEvent quitGameEvent = new PlayerQuitGameEvent(player, reason);
        Bukkit.getPluginManager().callEvent(quitGameEvent);

        WoolMatchStats matchStats = playerHolder.getMatchStats(player);

        if (isPlaying())
            shouldEnd = matchStats.getTeam().getOnlinePlayers().size() - 1 < MIN_PLAYERS_PER_TEAM;

        if (quitGameEvent.isSendMessage()) {
            String leaveMessage = ((ApplicablePreset<String, WoolMatch, Player, ChatPreset.AskingChatMotivation>) arena.getPreset(PresetType.CHAT))
                    .apply(this, player, ChatPreset.AskingChatMotivation.QUIT);

            for (Player matchPlayer : playerHolder.getOnlinePlayers()) {
                if (matchPlayer == player) {
                    matchPlayer.sendMessage(WoolWars.get().getLanguage().getProperty(LanguageFile.YOU_LEFT_FROM_THE_GAME));
                    continue;
                }

                matchPlayer.sendMessage(leaveMessage);
                WoolWars.get().getSettings().getProperty(ConfigFile.SOUNDS_PLAYER_LEFT).play(matchPlayer, 1, 1);
            }
        }

        if (matchStats.getTeam() != null) matchStats.getTeam().remove(player);

        woolPlayer.transferFrom(matchStats, false);
        playerHolder.removePlayer(woolPlayer);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer == player) continue;

            WoolPlayer woolPlayer1 = WoolPlayer.getWoolPlayer(onlinePlayer);
            if (woolPlayer1.isInMatch()) {
                player.hidePlayer(onlinePlayer);
                onlinePlayer.hidePlayer(player);
            } else {
                onlinePlayer.showPlayer(player);
                player.showPlayer(onlinePlayer);
            }
        }

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

            Npc npc = HCore.npcBuilder(UUIDUtils.getNewUUID())
                    .showEveryone(false)
                    .location(arena.getLocation(teamColor == TeamColor.RED ? ArenaLocationType.NPC_RED : ArenaLocationType.NPC_BLUE))
                    .lines(WoolWars.get().getLanguage().getProperty(LanguageFile.NPC_NAME))
                    .skin(new Skin(WoolWars.get().getLanguage().getProperty(LanguageFile.SKIN_TEXTURE), WoolWars.get().getLanguage().getProperty(LanguageFile.SKIN_SIGNATURE)))
                    .whenClicked(((player, action) -> {
                        if (playerHolder.getMatchStats(player) != null) {
                            new ClassSelectorGui(this, playerHolder.getMatchStats(player)).open(player);
                        }
                    }))
                    .build();

            team.setTeamNPC(npc);

            for (WoolPlayer woolPlayer : groups.get(i)) {
                team.join(woolPlayer, playerHolder.getMatchStats(woolPlayer.getName()));

                Player player = woolPlayer.toBukkitPlayer();
                for (String s : WoolWars.get().getLanguage().getProperty(LanguageFile.MATCH_START_INFORMATION)) {
                    player.sendMessage(s);
                }
            }

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

        playerHolder.forWoolPlayers(woolPlayer -> {
            Player player = woolPlayer.toBukkitPlayer();

            WoolMatchStats stats = playerHolder.getMatchStats(player);

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
            HCore.sendTitle(player, title.getTitle(), title.getSubTitle());

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

            if (isWinnerTeam) {
                WoolWars.get().getSettings().getProperty(ConfigFile.SOUNDS_GAME_WON).play(player, 1, 1);
            } else {
                WoolWars.get().getSettings().getProperty(ConfigFile.SOUNDS_GAME_LOST).play(player, 1, 1);
            }

        });

        roundHolder.getTasks().put(ResetMatchTask.ID, new ResetMatchTask(this,
                TimeUnit.SECONDS.toMillis(WoolWars.get().getSettings().getProperty(ConfigFile.CLOSE_GAME_COOLDOWN))).start());
    }

    @Override
    public void reset() {
        matchState = MatchState.RESETTING;

        for (WoolTeam value : teams.values()) value.getTeamNPC().delete();
        teams.clear();
        roundHolder.reset();
        playerHolder.reset();

        Bukkit.getScheduler().runTaskLater(WoolWars.get(), () -> matchState = MatchState.WAITING, 20 * 3L);
    }

    @Override
    public void pickUP(Player player, EntityPowerup entity, PowerUPType type) {
        playerHolder.getMatchStats(player).matchPowerUpsGotten++;
        player.sendMessage(WoolWars.get().getLanguage().getProperty(type.getProperty()).getPickupMessage());
        roundHolder.getEntities().remove(entity);

        WoolWars.get().getSettings().getProperty(ConfigFile.SOUNDS_POWERUP_COLLECTED).play(player, 1, 1);
    }

    @Override
    public void handleDeath(Player victim, Player killer, EntityDamageEvent.DamageCause cause) {
        if (!isPlaying() || isEnded()) {
            victim.teleport(arena.getLocation(ArenaLocationType.WAITING_LOBBY));
            WoolWars.get().getSettings().getProperty(ConfigFile.SOUNDS_TELEPORT).play(victim, 1, 1);
            return;
        }

        boolean wasASpectator = playerHolder.isSpectator(victim);
        boolean killedByAPlayer = killer != null && !playerHolder.isSpectator(killer);
        String diedMessage;

        WoolMatchStats victimStats = playerHolder.getMatchStats(victim);
        if (!wasASpectator) {
            playerHolder.setSpectator(victim);

            victimStats.matchDeaths++;
            victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 2, 1));

            Title title = WoolWars.get().getLanguage().getProperty(LanguageFile.DIED_TITLE);
            HCore.sendTitle(victim, title.getTitle(), title.getSubTitle());
        }

        if (killedByAPlayer) {
            Bukkit.getPluginManager().callEvent(new PlayerDeathByPlayerEvent(killer, victim));

            WoolMatchStats killerStats = playerHolder.getMatchStats(killer);
            killerStats.matchKills++;

            WoolWars.get().getSettings().getProperty(ConfigFile.SOUNDS_PLAYER_KILL).play(killer, 1, 1);
            diedMessage = WoolWars.get().getLanguage().getProperty(LanguageFile.KILL_BY_SOMEONE)
                    .replace("{victim}", victim.getName())
                    .replace("{victimTeamColor}", victimStats.getTeam().getTeamColor().getCC().toString())
                    .replace("{killer}", killer.getName())
                    .replace("{killerTeamColor}", killerStats.getTeam().getTeamColor().getCC().toString());
        } else {
            Bukkit.getPluginManager().callEvent(new PlayerDeathEvent(victim));
            Property<String> property;

            switch (cause) {
                case LAVA: {
                    property = LanguageFile.DIED_FROM_LAVA;
                    break;
                }
                case VOID: {
                    property = LanguageFile.DIED_FROM_VOID;
                    break;
                }
                default: {
                    property = LanguageFile.DIED;
                }
            }

            diedMessage = WoolWars.get().getLanguage().getProperty(property)
                    .replace("{victim}", victim.getName())
                    .replace("{victimTeamColor}", victimStats.getTeam().getTeamColor().getCC().toString());
        }

        if (cause == EntityDamageEvent.DamageCause.VOID) {
            victim.teleport(arena.getLocation(ArenaLocationType.WAITING_LOBBY));
            WoolWars.get().getSettings().getProperty(ConfigFile.SOUNDS_TELEPORT).play(victim, 1, 1);
        }

        if (!wasASpectator) {
            WoolWars.get().getSettings().getProperty(ConfigFile.SOUNDS_PLAYER_DEATH).play(victim, 1, 1);
            for (Player onlinePlayer : playerHolder.getOnlinePlayers()) {
                onlinePlayer.sendMessage(diedMessage);
            }
        }


        Set<Player> alivePlayers = playerHolder.getGamePlayers();
        if (alivePlayers.isEmpty()) {
            Region centerRegion = arena.getRegion(ArenaRegionType.CENTER);

            Map<DyeColor, Integer> blockPlacedPerTeamColor = new HashMap<>();
            for (Block block : centerRegion.getBlocks()) {
                if (!block.getType().toString().contains("WOOL")) continue;

                DyeColor dyeColor = DyeColor.getByWoolData(block.getData());
                blockPlacedPerTeamColor.putIfAbsent(dyeColor, 0);
                blockPlacedPerTeamColor.put(dyeColor, blockPlacedPerTeamColor.get(dyeColor) + 1);
            }

            List<DyeColor> colors = TeamUtils.getTopTeamPlacedByWoolColor(blockPlacedPerTeamColor);
            if (blockPlacedPerTeamColor.isEmpty() || TeamUtils.testBlocksColorTruth(colors, blockPlacedPerTeamColor))
                return;
            roundHolder.endRound(teams.get(TeamColor.fromDyeColor(colors.get(0))));
        }
    }

    @Override
    public int getMaxPlayers() {
        return arena.getMaxPlayers();
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
        return matchState == MatchState.ENDING || matchState == MatchState.RESETTING;
    }
}

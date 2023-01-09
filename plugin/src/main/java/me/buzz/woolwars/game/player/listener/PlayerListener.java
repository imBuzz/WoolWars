package me.buzz.woolwars.game.player.listener;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import me.buzz.woolwars.api.player.QuitGameReason;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.ConfigFile;
import me.buzz.woolwars.game.configuration.files.lang.LanguageFile;
import me.buzz.woolwars.game.game.arena.location.SerializedLocation;
import me.buzz.woolwars.game.game.arena.settings.preset.ApplicablePreset;
import me.buzz.woolwars.game.game.arena.settings.preset.PresetType;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.hook.ImplementedHookType;
import me.buzz.woolwars.game.hook.hooks.placeholderapi.PlaceholderAPIHook;
import me.buzz.woolwars.game.player.WoolPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerListener implements Listener {

    private final BukkitRunnable scoreboardUpdateTask;

    public PlayerListener() {
        scoreboardUpdateTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    BPlayerBoard board = Netherboard.instance().getBoard(onlinePlayer);
                    if (board == null) return;

                    applyScoreboardLines(getScoreboardLinesByMatchState(onlinePlayer, WoolWars.get().getGameManager().getInternalMatchByPlayer(onlinePlayer)), board);
                }
            }
        };

        scoreboardUpdateTask.runTaskTimerAsynchronously(WoolWars.get(), 50L, 10L);
    }

    @EventHandler
    public void prepareLocation(PlayerSpawnLocationEvent event) {
        SerializedLocation location = WoolWars.get().getSettings().getProperty(ConfigFile.LOBBY_LOCATION);
        event.setSpawnLocation(location.toBukkitLocation(Bukkit.getWorld(location.getWorldName())));
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (WoolWars.get().getSettings().getProperty(ConfigFile.ENABLE_NATIVE_SCOREBOARD)) {
            Netherboard.instance().createBoard(player, WoolWars.get().getLanguage().getProperty(LanguageFile.SCOREBOARD_TITLE));
        }

        WoolWars.get().getDataProvider().loadPlayer(player).whenComplete((woolPlayer, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            }

            WoolPlayer.trackPlayer(woolPlayer);
        });

        for (WoolPlayer woolOnlinePlayer : WoolPlayer.getWoolOnlinePlayers()) {
            if (woolOnlinePlayer.toBukkitPlayer() == player) continue;

            if (woolOnlinePlayer.isInMatch()) {
                player.hidePlayer(woolOnlinePlayer.toBukkitPlayer());
                woolOnlinePlayer.toBukkitPlayer().hidePlayer(player);
            }
        }
    }

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        WoolMatch woolMatch = WoolWars.get().getGameManager().getInternalMatchByPlayer(event.getPlayer());
        if (woolMatch != null) woolMatch.quit(WoolPlayer.getWoolPlayer(event.getPlayer()), QuitGameReason.DISCONNECT);

        WoolWars.get().getDataProvider().savePlayer(WoolPlayer.removePlayer(event.getPlayer()));
    }

    private List<String> getScoreboardLinesByMatchState(Player player, WoolMatch match) {
        if (match == null) {
            List<String> strings = WoolWars.get().getLanguage().getProperty(LanguageFile.SCOREBOARD_MATCH_LOBBY), tempLines = new ArrayList<>();
            PlaceholderAPIHook placeholderHook = WoolWars.get().getHook(ImplementedHookType.PLACEHOLDER_API);
            for (String line : strings) {
                tempLines.add(placeholderHook != null ? placeholderHook.apply(line, player) : line);
            }
            return tempLines;
        }

        return ((ApplicablePreset<List<String>, WoolMatch, Player, Void>) match.getPlayableArena().getPreset(PresetType.SCOREBOARD))
                .apply(match, player, null);
    }

    private void applyScoreboardLines(List<String> lines, BPlayerBoard board) {
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            board.set(line, lines.size() - i);
        }

        Set<Integer> scores = new HashSet<>(board.getLines().keySet());
        for (int score : scores) {
            if (score <= 0 || score > lines.size()) {
                board.remove(score);
            }
        }
    }

}

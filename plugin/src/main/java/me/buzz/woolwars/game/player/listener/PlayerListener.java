package me.buzz.woolwars.game.player.listener;

import com.google.common.collect.ImmutableList;
import com.hakan.core.HCore;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import fr.mrmicky.fastboard.FastBoard;
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
import me.buzz.woolwars.game.hook.hooks.viaversion.ViaVersionAPIHook;
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
import java.util.Collections;
import java.util.List;

public class PlayerListener implements Listener {

    private final WoolWars plugin = WoolWars.get();
    private final BukkitRunnable scoreboardUpdateTask;

    public PlayerListener() {
        scoreboardUpdateTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (WoolPlayer woolPlayer : WoolPlayer.getWoolOnlinePlayers()) {
                    FastBoard board = woolPlayer.getBoard();
                    if (board == null)
                        continue;

                    Player player = woolPlayer.toBukkitPlayer();
                    if (player == null)
                        continue;

                    applyScoreboardLines(getScoreboardLinesByMatchState(player,
                            WoolWars.get().getGameManager().getInternalMatchByPlayer(player)), board);
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
        WoolWars.get().getDataProvider().loadPlayer(player).whenComplete((woolPlayer, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            }

            WoolPlayer.trackPlayer(woolPlayer);
            if (WoolWars.get().getSettings().getProperty(ConfigFile.ENABLE_NATIVE_SCOREBOARD)) {
                FastBoard board = new FastBoard(player) {
                    @Override
                    protected boolean hasLinesMaxLength() {
                        ViaVersionAPIHook viaVersionAPIHook = plugin.getHook(ImplementedHookType.VIA_VERSION);
                        if (viaVersionAPIHook == null)
                            return true;

                        return viaVersionAPIHook.getPlayerMinecraftVersion(getPlayer()) < ProtocolVersion.v1_13.getVersion();
                    }
                };
                board.updateTitle(WoolWars.get().getLanguage().getProperty(LanguageFile.SCOREBOARD_TITLE));

                woolPlayer.setBoard(board);
            }
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

        WoolPlayer woolPlayer = WoolPlayer.removePlayer(event.getPlayer());
        woolPlayer.getBoard().delete();

        WoolWars.get().getDataProvider().savePlayer(woolPlayer);
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

    private void applyScoreboardLines(List<String> lines, FastBoard board) {
        for (int i = 0; i < board.getLines().size(); i++)
            board.removeLine(i);

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            board.updateLine(i, line);
        }
    }

}
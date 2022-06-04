package me.buzz.woolwars.game.player.listener;

import com.hakan.core.scoreboard.HScoreboard;
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
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements Listener {

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
        });
        createScoreboard(player);

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

    public void createScoreboard(Player player) {
        HScoreboard scoreboard = HScoreboard.create(player);

        scoreboard.setTitle(WoolWars.get().getLanguage().getProperty(LanguageFile.SCOREBOARD_TITLE));
        scoreboard.setUpdateInterval(10);
        scoreboard.show();

        scoreboard.update(hScoreboard -> scoreboard.setLines(getScoreboardLinesByMatchState(player, WoolWars.get().getGameManager().getInternalMatchByPlayer(player))));
    }

    public List<String> getScoreboardLinesByMatchState(Player player, WoolMatch match) {
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

}

package me.buzz.woolwars.game.player.listener;

import fr.minuskube.netherboard.Netherboard;
import me.buzz.woolwars.api.player.QuitGameReason;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.ConfigFile;
import me.buzz.woolwars.game.configuration.files.lang.LanguageFile;
import me.buzz.woolwars.game.game.arena.location.SerializedLocation;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.player.WoolPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

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
        Netherboard.instance().createBoard(player, WoolWars.get().getLanguage().getProperty(LanguageFile.SCOREBOARD_TITLE));

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
        WoolMatch woolMatch = WoolWars.get().getGameManager().getInternalMatchByPlayer(event.getPlayer());
        if (woolMatch != null)
            woolMatch.quit(WoolPlayer.getWoolPlayer(event.getPlayer()), QuitGameReason.DISCONNECT);
        WoolWars.get().getDataProvider().savePlayer(WoolPlayer.removePlayer(event.getPlayer()));
    }

}

package me.buzz.woolwars.game.player.listener;

import fr.minuskube.netherboard.Netherboard;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.player.WoolPlayer;
import me.buzz.woolwars.game.utils.StringsUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent event) {
        Netherboard.instance().createBoard(event.getPlayer(), StringsUtils.colorize("&e&lWOOL WARS"));
        WoolWars.get().getDataProvider().loadPlayer(event.getPlayer()).whenComplete((woolPlayer, throwable) -> WoolPlayer.trackPlayer(woolPlayer));
    }

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        WoolWars.get().getDataProvider().savePlayer(WoolPlayer.removePlayer(event.getPlayer()));
    }

}

package me.buzz.woolwars.game.player;

import fr.minuskube.netherboard.Netherboard;
import me.buzz.woolwars.game.utils.StringsUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent event) {
        Netherboard.instance().createBoard(event.getPlayer(), StringsUtils.colorize("&e&lWOOL WARS"));
    }

}

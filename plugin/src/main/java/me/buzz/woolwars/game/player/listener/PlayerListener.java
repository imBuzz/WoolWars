package me.buzz.woolwars.game.player.listener;

import fr.minuskube.netherboard.Netherboard;
import me.buzz.woolwars.api.player.QuitGameReason;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.lang.LanguageFile;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.player.WoolPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent event) {
        Netherboard.instance().createBoard(event.getPlayer(), WoolWars.get().getLanguage().getProperty(LanguageFile.SCOREBOARD_TITLE));
        WoolWars.get().getDataProvider().loadPlayer(event.getPlayer()).whenComplete((woolPlayer, throwable) -> WoolPlayer.trackPlayer(woolPlayer));
    }

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        WoolMatch woolMatch = WoolWars.get().getGameManager().getInternalMatchByPlayer(event.getPlayer());
        if (woolMatch != null)
            woolMatch.quit(WoolPlayer.getWoolPlayer(event.getPlayer()), QuitGameReason.DISCONNECT);
        WoolWars.get().getDataProvider().savePlayer(WoolPlayer.removePlayer(event.getPlayer()));
    }

}

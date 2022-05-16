package me.buzz.woolwars.game.data;

import me.buzz.woolwars.game.player.WoolPlayer;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public interface DataProvider {

    void init();

    CompletableFuture<WoolPlayer> loadPlayer(Player player);

    void savePlayer(WoolPlayer woolPlayer);

    DataProviderType getType();

}

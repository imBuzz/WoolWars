package me.buzz.woolwars.game.game.match.player.team;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;

public interface MatchTeam {

    List<Player> getOnlinePlayers();

    default void message(String message) {
        forEach(player -> player.sendMessage(message));
    }

    default void forEach(Consumer<Player> consumer) {
        getOnlinePlayers().forEach(consumer);
    }

    default boolean isOnline(Player player) {
        return getOnlinePlayers().contains(player);
    }

}

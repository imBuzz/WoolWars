package me.buzz.woolwars.api.game.match.player.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerJoinGameEvent extends Event implements Cancellable {

    @Getter private final Player player;
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    @Getter @Setter private boolean isCancelled = false;

    public PlayerJoinGameEvent(Player player){
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

}

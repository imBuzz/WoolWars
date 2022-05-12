package me.buzz.woolwars.api.events;

import lombok.Getter;
import lombok.Setter;
import me.buzz.woolwars.api.player.QuitGameReason;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerQuitGameEvent extends Event {

    @Getter private final Player player;
    @Getter private final QuitGameReason reason;
    @Getter @Setter private boolean sendMessage = true;

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public PlayerQuitGameEvent(Player player, QuitGameReason reason){
        this.player = player;
        this.reason = reason;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

}

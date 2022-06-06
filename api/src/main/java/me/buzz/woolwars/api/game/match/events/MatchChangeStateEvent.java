package me.buzz.woolwars.api.game.match.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.buzz.woolwars.api.game.match.ApiMatch;
import me.buzz.woolwars.api.game.match.state.MatchState;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
public class MatchChangeStateEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    @Getter
    private final ApiMatch match;
    @Getter
    private final MatchState oldState, newState;

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

}

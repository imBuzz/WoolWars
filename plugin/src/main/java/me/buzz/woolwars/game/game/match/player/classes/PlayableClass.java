package me.buzz.woolwars.game.game.match.player.classes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.buzz.woolwars.game.game.match.player.team.color.TeamColor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public abstract class PlayableClass {

    protected final Player player;
    protected final TeamColor color;
    @Getter
    protected boolean used = false;

    public abstract void equip();

    public abstract void useAbility();

}

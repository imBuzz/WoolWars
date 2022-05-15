package me.buzz.woolwars.game.game.match.player.classes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.buzz.woolwars.api.game.match.player.player.classes.PlayableClassType;
import me.buzz.woolwars.game.game.match.player.stats.MatchStats;
import me.buzz.woolwars.game.game.match.player.team.color.TeamColor;
import me.buzz.woolwars.game.player.WoolPlayer;
import me.buzz.woolwars.game.utils.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public abstract class PlayableClass {

    protected final Player player;
    protected final TeamColor color;
    @Getter
    protected final PlayableClassType type;
    @Getter
    protected boolean used = false;

    public static ItemStack adjustItem(TeamColor team, ItemStack itemStack) {
        switch (itemStack.getType()) {
            case WOOL: {
                return new ItemBuilder(itemStack).wool(team.getDC()).build();
            }
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS: {
                return new ItemBuilder(itemStack).setLeatherColor(team.getDC().getColor()).build();
            }
            default:
                return itemStack;
        }
    }

    public abstract void useAbility();

    public abstract void equip(WoolPlayer woolPlayer, MatchStats stats);

    public abstract void reset();

}

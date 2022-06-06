package me.buzz.woolwars.game.game.match.player.classes;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.buzz.woolwars.api.game.match.player.player.classes.PlayableClassType;
import me.buzz.woolwars.api.game.match.player.team.TeamColor;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.player.stats.WoolMatchStats;
import me.buzz.woolwars.game.player.WoolPlayer;
import me.buzz.woolwars.game.utils.structures.itembuilder.LeatherItemBuilder;
import me.buzz.woolwars.game.utils.structures.itembuilder.PotionAndWoolItemBuilder;
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
        XMaterial material = XMaterial.matchXMaterial(itemStack);
        switch (material) {
            case WHITE_WOOL: {
                return new PotionAndWoolItemBuilder(itemStack).setColor(team.getDC()).build();
            }
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS: {
                return new LeatherItemBuilder(itemStack).setLeatherColor(team.getDC().getColor()).build();
            }
            default:
                return itemStack;
        }
    }

    public abstract void useAbility(WoolMatch match, Player player);

    public abstract void equip(WoolPlayer woolPlayer, WoolMatchStats stats);

    public abstract void reset();

}

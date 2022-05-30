package me.buzz.woolwars.game.game.match.player.classes.classes;

import me.buzz.woolwars.api.game.match.player.player.classes.PlayableClassType;
import me.buzz.woolwars.api.game.match.player.team.TeamColor;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.lang.LanguageFile;
import me.buzz.woolwars.game.game.match.player.classes.PlayableClass;
import me.buzz.woolwars.game.game.match.player.equipment.ArmorSlot;
import me.buzz.woolwars.game.game.match.player.stats.WoolMatchStats;
import me.buzz.woolwars.game.player.WoolPlayer;
import me.buzz.woolwars.game.utils.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import java.util.HashMap;
import java.util.Map;

public class SwordmanPlayableClass extends PlayableClass {

    private final static Map<ArmorSlot, ItemStack> armor = new HashMap<>();
    private final static Map<Integer, ItemStack> items = new HashMap<>();

    static {
        items.put(0, new ItemBuilder(Material.STONE_SWORD).setFlags(ItemFlag.HIDE_UNBREAKABLE).setUnbreakable(true).build());
        items.put(1, new ItemBuilder(Material.WOOD_PICKAXE).setFlags(ItemFlag.HIDE_UNBREAKABLE).setUnbreakable(true).build());
        items.put(2, new ItemBuilder(Material.SHEARS).setFlags(ItemFlag.HIDE_UNBREAKABLE).setUnbreakable(true).build());

        items.put(3, new ItemBuilder(Material.POTION).potion(PotionType.INSTANT_HEAL, 2, true).build());
        items.put(4, new ItemBuilder(Material.WOOL, 64).build());

        items.put(8, WoolWars.get().getLanguage().getProperty(LanguageFile.SWORDMAN_KEYSTONE)
                .toItemStack());

        armor.put(ArmorSlot.HELMET, new ItemStack(Material.AIR));
        armor.put(ArmorSlot.CHESTPLATE, new ItemStack(Material.AIR));
        armor.put(ArmorSlot.LEGGINGS, new ItemBuilder(Material.LEATHER_LEGGINGS).setFlags(ItemFlag.HIDE_UNBREAKABLE).setUnbreakable(true).build());
        armor.put(ArmorSlot.BOOTS, new ItemBuilder(Material.LEATHER_BOOTS).setFlags(ItemFlag.HIDE_UNBREAKABLE).setUnbreakable(true).build());
    }

    public SwordmanPlayableClass(Player player, TeamColor teamColor) {
        super(player, teamColor, PlayableClassType.SWORDMAN);
    }

    public static String getBaseLayout() {
        return "123450009";
    }

    @Override
    public void equip(WoolPlayer woolPlayer, WoolMatchStats stats) {
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().setArmorContents(null);
        player.getInventory().clear();

        TeamColor teamColor = stats.getTeam().getTeamColor();
        armor.forEach((slot, item) -> slot.getAction().accept(player, PlayableClass.adjustItem(teamColor, item.clone())));

        String[] slots = woolPlayer.getKitLayout(getType()).split("");
        for (String stringSlot : slots) {
            int slot = Integer.parseInt(stringSlot) - 1;
            if (slot < 0) continue;
            player.getInventory().setItem(slot, PlayableClass.adjustItem(teamColor, items.get(slot).clone()));
        }
    }

    @Override
    public void useAbility() {

    }

    @Override
    public void reset() {
        used = false;
    }

}

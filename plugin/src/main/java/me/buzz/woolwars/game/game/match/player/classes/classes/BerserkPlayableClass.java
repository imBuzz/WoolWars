package me.buzz.woolwars.game.game.match.player.classes.classes;

import com.google.common.collect.Lists;
import me.buzz.woolwars.api.game.match.player.player.classes.PlayableClassType;
import me.buzz.woolwars.game.game.match.player.classes.PlayableClass;
import me.buzz.woolwars.game.game.match.player.equipment.ArmorSlot;
import me.buzz.woolwars.game.game.match.player.team.color.TeamColor;
import me.buzz.woolwars.game.utils.ItemBuilder;
import me.buzz.woolwars.game.utils.structures.Pair;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BerserkPlayableClass extends PlayableClass {
    private final static List<ItemStack> globalItems = new ArrayList<>();
    private final static Map<TeamColor, List<Pair<ArmorSlot, ItemStack>>> coloredEquipment = new HashMap<>();

    static {
        globalItems.add(new ItemStack(Material.STONE_SWORD));
        globalItems.add(new ItemStack(Material.WOOD_PICKAXE));
        globalItems.add(new ItemStack(Material.SHEARS));

        coloredEquipment.put(TeamColor.RED, Lists.newArrayList(
                new Pair<>(ArmorSlot.LEGGINGS, new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherColor(Color.RED).build()),
                new Pair<>(ArmorSlot.OTHER, new Wool(DyeColor.RED).toItemStack())
        ));

        coloredEquipment.put(TeamColor.BLUE, Lists.newArrayList(
                new Pair<>(ArmorSlot.LEGGINGS, new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherColor(Color.BLUE).build()),
                new Pair<>(ArmorSlot.OTHER, new Wool(DyeColor.BLUE).toItemStack())
        ));
    }

    public BerserkPlayableClass(Player player, TeamColor teamColor) {
        super(player, teamColor, PlayableClassType.BERSERK);
    }

    @Override
    public void equip() {
        player.getInventory().setArmorContents(null);
        player.getInventory().clear();

        for (ItemStack globalItem : globalItems) {
            player.getInventory().addItem(globalItem.clone());
        }

        List<Pair<ArmorSlot, ItemStack>> equipment = coloredEquipment.get(color);
        for (Pair<ArmorSlot, ItemStack> pair : equipment) {
            if (pair.getKey() == ArmorSlot.OTHER) player.getInventory().addItem(pair.getValue().clone());
            else player.getInventory().setItem(pair.getKey().getSlot(), pair.getValue().clone());
        }
    }

    @Override
    public void useAbility() {
        if (used) return;
        used = true;
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 3, 1));
    }

}

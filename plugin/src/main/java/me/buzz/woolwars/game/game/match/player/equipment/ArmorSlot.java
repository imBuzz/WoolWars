package me.buzz.woolwars.game.game.match.player.equipment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

@RequiredArgsConstructor
@Getter
public enum ArmorSlot {

    HELMET((player, itemStack) -> player.getInventory().setHelmet(itemStack)),
    CHESTPLATE((player, itemStack) -> player.getInventory().setChestplate(itemStack)),
    LEGGINGS((player, itemStack) -> player.getInventory().setLeggings(itemStack)),
    BOOTS((player, itemStack) -> player.getInventory().setBoots(itemStack));


    private final BiConsumer<Player, ItemStack> action;

}

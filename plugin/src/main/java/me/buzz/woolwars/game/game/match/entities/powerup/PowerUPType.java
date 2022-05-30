package me.buzz.woolwars.game.game.match.entities.powerup;

import ch.jalu.configme.properties.Property;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.buzz.woolwars.game.configuration.files.lang.LanguageFile;
import me.buzz.woolwars.game.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.EulerAngle;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Getter
public enum PowerUPType {

    STONE_PICKAXE(LanguageFile.STONE_PICKAXE_POWERUP, player -> {
        boolean hasItem = false;
        int slot = 99;

        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = player.getInventory().getItem(i);
            if (itemStack != null) {
                if (itemStack.getType() == Material.STONE_PICKAXE) {
                    hasItem = true;
                    continue;
                }
                if (itemStack.getType().toString().endsWith("_PICKAXE")) slot = i;
            }
        }
        if (!hasItem) {
            if (slot != 99) {
                player.getInventory().setItem(slot, new ItemBuilder(Material.STONE_PICKAXE).setFlags(ItemFlag.HIDE_UNBREAKABLE).setUnbreakable(true).build());
            } else {
                player.getInventory().addItem(new ItemBuilder(Material.STONE_PICKAXE).setFlags(ItemFlag.HIDE_UNBREAKABLE).setUnbreakable(true).build());
            }
        }
    }, armorStand -> {
        armorStand.setItemInHand(new ItemStack(Material.STONE_PICKAXE));
        armorStand.setRightArmPose(new EulerAngle(Math.toRadians(280), Math.toRadians(100), 0));
    }),
    STONE_SWORD(LanguageFile.STONE_SWORD_POWERUP, player -> {
        boolean hasItem = false;
        int slot = 99;

        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = player.getInventory().getItem(i);
            if (itemStack != null) {
                if (itemStack.getType() == Material.STONE_SWORD) {
                    hasItem = true;
                    continue;
                }
                if (itemStack.getType().toString().endsWith("_SWORD")) slot = i;
            }
        }
        if (!hasItem) {
            if (slot != 99) {
                player.getInventory().setItem(slot, new ItemBuilder(Material.STONE_SWORD).setFlags(ItemFlag.HIDE_UNBREAKABLE).setUnbreakable(true).build());
            } else {
                player.getInventory().addItem(new ItemBuilder(Material.STONE_SWORD).setFlags(ItemFlag.HIDE_UNBREAKABLE).setUnbreakable(true).build());
            }
        }
    }, armorStand -> {
        armorStand.setItemInHand(new ItemStack(Material.STONE_SWORD));
        armorStand.setRightArmPose(new EulerAngle(Math.toRadians(280), Math.toRadians(100), 0));
    }),
    BOW(LanguageFile.BOW_POWERUP, player -> {
        boolean hasItem = false;
        int slot = 99;

        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = player.getInventory().getItem(i);
            if (itemStack != null) {
                if (itemStack.getType() == Material.BOW) {
                    hasItem = true;
                    continue;
                }
                if (itemStack.getType().toString().endsWith("BOW")) slot = i;
            }
        }
        if (!hasItem) {
            if (slot != 99) {
                player.getInventory().setItem(slot, new ItemBuilder(Material.BOW).setFlags(ItemFlag.HIDE_UNBREAKABLE).setUnbreakable(true).build());
                player.getInventory().addItem(new ItemStack(Material.ARROW, 2));
            } else {
                player.getInventory().addItem(new ItemBuilder(Material.BOW).setFlags(ItemFlag.HIDE_UNBREAKABLE).setUnbreakable(true).build());
                player.getInventory().addItem(new ItemStack(Material.ARROW, 2));
            }
        }
    }, armorStand -> {
        armorStand.setItemInHand(new ItemStack(Material.BOW));
        armorStand.setRightArmPose(new EulerAngle(Math.toRadians(280), Math.toRadians(100), 0));
    }),

    INSTANT_HEAL(LanguageFile.INSTANT_HEAL_POWERUP, player -> player.setHealth(20), armorStand -> {
        armorStand.setItemInHand(new ItemBuilder(Material.POTION).potion(PotionType.INSTANT_HEAL, 1, false).build());
        armorStand.setRightArmPose(new EulerAngle(Math.toRadians(280), Math.toRadians(100), 0));
    }),
    STRENGTH(LanguageFile.STRENGTH_POWERUP, player -> player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 3, 1)), armorStand -> {
        armorStand.setItemInHand(new ItemBuilder(Material.POTION).potion(PotionType.STRENGTH, 1, false).build());
        armorStand.setRightArmPose(new EulerAngle(Math.toRadians(280), Math.toRadians(100), 0));
    }),
    SPEED(LanguageFile.SPEED_POWERUP, player -> player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 3, 2)), armorStand -> {
        armorStand.setItemInHand(new ItemBuilder(Material.POTION).potion(PotionType.SPEED, 1, false).build());
        armorStand.setRightArmPose(new EulerAngle(Math.toRadians(280), Math.toRadians(100), 0));
    }),
    JUMP_BOOST(LanguageFile.JUMP_BOOST_POWERUP, player -> player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 3, 4)), armorStand -> {
        armorStand.setItemInHand(new ItemBuilder(Material.POTION).potion(PotionType.JUMP, 1, false).build());
        armorStand.setRightArmPose(new EulerAngle(Math.toRadians(280), Math.toRadians(100), 0));
    }),

    CHAINMAIL_HELMET(LanguageFile.CHAINMAIL_HELMET_POWERUP, player -> {
        boolean hasItem = player.getInventory().getHelmet() != null && player.getInventory().getBoots().getType() == Material.CHAINMAIL_HELMET;
        if (!hasItem)
            player.getInventory().setHelmet(new ItemBuilder(Material.CHAINMAIL_HELMET).setFlags(ItemFlag.HIDE_UNBREAKABLE).setUnbreakable(true).build());
    }, armorStand -> {
        armorStand.setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
    }),
    CHAINMAIL_CHESTPLATE(LanguageFile.CHAINMAIL_CHESTPLATE_POWERUP, player -> {
        boolean hasItem = player.getInventory().getChestplate() != null && player.getInventory().getBoots().getType() == Material.CHAINMAIL_CHESTPLATE;
        if (!hasItem)
            player.getInventory().setChestplate(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).setFlags(ItemFlag.HIDE_UNBREAKABLE).setUnbreakable(true).build());
    }, armorStand -> {
        armorStand.setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        armorStand.teleport(armorStand.getLocation().clone().add(0, 0.5, 0));
    }),
    CHAINMAIL_BOOTS(LanguageFile.CHAINMAIL_BOOTS_POWERUP, player -> {
        boolean hasItem = player.getInventory().getBoots() != null && player.getInventory().getBoots().getType() == Material.CHAINMAIL_BOOTS;
        if (!hasItem)
            player.getInventory().setBoots(new ItemBuilder(Material.CHAINMAIL_BOOTS).setFlags(ItemFlag.HIDE_UNBREAKABLE).setUnbreakable(true).build());
    }, armorStand -> {
        armorStand.setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
        armorStand.teleport(armorStand.getLocation().clone().add(0, 1.5, 0));
    }),
    IRON_BOOTS(LanguageFile.IRON_BOOTS_POWERUP, player -> {
        boolean hasItem = player.getInventory().getBoots() != null && player.getInventory().getBoots().getType() == Material.IRON_BOOTS;
        if (!hasItem)
            player.getInventory().setBoots(new ItemBuilder(Material.IRON_BOOTS).setFlags(ItemFlag.HIDE_UNBREAKABLE).setUnbreakable(true).build());
    }, armorStand -> {
        armorStand.setBoots(new ItemStack(Material.IRON_BOOTS));
        armorStand.teleport(armorStand.getLocation().clone().add(0, 1.5, 0));
    });

    private final Property<ConfigurablePowerup> property;
    private final Consumer<Player> action;
    private final Consumer<ArmorStand> adjust;

}
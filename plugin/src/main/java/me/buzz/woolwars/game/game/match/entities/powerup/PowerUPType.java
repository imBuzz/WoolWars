package me.buzz.woolwars.game.game.match.entities.powerup;

import ch.jalu.configme.properties.Property;
import com.cryptomorin.xseries.XMaterial;
import com.hakan.core.item.HItemBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.buzz.woolwars.game.configuration.files.lang.LanguageFile;
import me.buzz.woolwars.game.utils.structures.itembuilder.PotionAndWoolItemBuilder;
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
        Material material = XMaterial.STONE_PICKAXE.parseMaterial();

        boolean hasItem = false;
        int slot = 99;

        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = player.getInventory().getItem(i);
            if (itemStack != null) {
                if (itemStack.getType() == XMaterial.STONE_PICKAXE.parseMaterial()) {
                    hasItem = true;
                    continue;
                }
                if (itemStack.getType().toString().endsWith("_PICKAXE")) slot = i;
            }
        }
        if (!hasItem) {
            if (slot != 99) {
                player.getInventory().setItem(slot, new HItemBuilder(material).addItemFlags(ItemFlag.HIDE_UNBREAKABLE).unbreakable(true).build());
            } else {
                player.getInventory().addItem(new HItemBuilder(material).addItemFlags(ItemFlag.HIDE_UNBREAKABLE).unbreakable(true).build());
            }
        }
    }, armorStand -> {
        armorStand.setItemInHand(XMaterial.STONE_PICKAXE.parseItem());
        armorStand.setRightArmPose(new EulerAngle(Math.toRadians(280), Math.toRadians(100), 0));
    }),
    STONE_SWORD(LanguageFile.STONE_SWORD_POWERUP, player -> {
        Material material = XMaterial.STONE_SWORD.parseMaterial();
        boolean hasItem = false;
        int slot = 99;

        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = player.getInventory().getItem(i);
            if (itemStack != null) {
                if (itemStack.getType() == material) {
                    hasItem = true;
                    continue;
                }
                if (itemStack.getType().toString().endsWith("_SWORD")) slot = i;
            }
        }
        if (!hasItem) {
            if (slot != 99) {
                player.getInventory().setItem(slot, new HItemBuilder(material).addItemFlags(ItemFlag.HIDE_UNBREAKABLE).unbreakable(true).build());
            } else {
                player.getInventory().addItem(new HItemBuilder(material).addItemFlags(ItemFlag.HIDE_UNBREAKABLE).unbreakable(true).build());
            }
        }
    }, armorStand -> {
        armorStand.setItemInHand(XMaterial.STONE_SWORD.parseItem());
        armorStand.setRightArmPose(new EulerAngle(Math.toRadians(280), Math.toRadians(100), 0));
    }),
    BOW(LanguageFile.BOW_POWERUP, player -> {
        Material material = XMaterial.BOW.parseMaterial();
        boolean hasItem = false;
        int slot = 99;

        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = player.getInventory().getItem(i);
            if (itemStack != null) {
                if (itemStack.getType() == material) {
                    hasItem = true;
                    continue;
                }
                if (itemStack.getType().toString().endsWith("BOW")) slot = i;
            }
        }
        if (!hasItem) {
            if (slot != 99) {
                player.getInventory().setItem(slot, new HItemBuilder(material).addItemFlags(ItemFlag.HIDE_UNBREAKABLE).unbreakable(true).build());
                player.getInventory().addItem(new ItemStack(Material.ARROW, 2));
            } else {
                player.getInventory().addItem(new HItemBuilder(material).addItemFlags(ItemFlag.HIDE_UNBREAKABLE).unbreakable(true).build());
                player.getInventory().addItem(new ItemStack(Material.ARROW, 2));
            }
        }
    }, armorStand -> {
        armorStand.setItemInHand(XMaterial.BOW.parseItem());
        armorStand.setRightArmPose(new EulerAngle(Math.toRadians(280), Math.toRadians(100), 0));
    }),

    INSTANT_HEAL(LanguageFile.INSTANT_HEAL_POWERUP, player -> player.setHealth(20), armorStand -> {
        armorStand.setItemInHand(new PotionAndWoolItemBuilder(Material.POTION).potion(PotionType.INSTANT_HEAL, 1, false).build());
        armorStand.setRightArmPose(new EulerAngle(Math.toRadians(280), Math.toRadians(100), 0));
    }),
    STRENGTH(LanguageFile.STRENGTH_POWERUP, player -> player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 3, 1)), armorStand -> {
        armorStand.setItemInHand(new PotionAndWoolItemBuilder(Material.POTION).potion(PotionType.STRENGTH, 1, false).build());
        armorStand.setRightArmPose(new EulerAngle(Math.toRadians(280), Math.toRadians(100), 0));
    }),
    SPEED(LanguageFile.SPEED_POWERUP, player -> player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 3, 2)), armorStand -> {
        armorStand.setItemInHand(new PotionAndWoolItemBuilder(Material.POTION).potion(PotionType.SPEED, 1, false).build());
        armorStand.setRightArmPose(new EulerAngle(Math.toRadians(280), Math.toRadians(100), 0));
    }),
    JUMP_BOOST(LanguageFile.JUMP_BOOST_POWERUP, player -> player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 3, 4)), armorStand -> {
        armorStand.setItemInHand(new PotionAndWoolItemBuilder(Material.POTION).potion(PotionType.JUMP, 1, false).build());
        armorStand.setRightArmPose(new EulerAngle(Math.toRadians(280), Math.toRadians(100), 0));
    }),

    CHAINMAIL_HELMET(LanguageFile.CHAINMAIL_HELMET_POWERUP, player -> {
        boolean hasItem = player.getInventory().getHelmet() != null && player.getInventory().getBoots().getType() == XMaterial.CHAINMAIL_HELMET.parseMaterial();
        if (!hasItem)
            player.getInventory().setHelmet(new HItemBuilder(XMaterial.CHAINMAIL_HELMET.parseMaterial()).addItemFlags(ItemFlag.HIDE_UNBREAKABLE).unbreakable(true).build());
    }, armorStand -> armorStand.setHelmet(XMaterial.CHAINMAIL_HELMET.parseItem())),
    CHAINMAIL_CHESTPLATE(LanguageFile.CHAINMAIL_CHESTPLATE_POWERUP, player -> {
        boolean hasItem = player.getInventory().getChestplate() != null && player.getInventory().getBoots().getType() == XMaterial.CHAINMAIL_CHESTPLATE.parseMaterial();
        if (!hasItem)
            player.getInventory().setChestplate(new HItemBuilder(XMaterial.CHAINMAIL_CHESTPLATE.parseMaterial()).addItemFlags(ItemFlag.HIDE_UNBREAKABLE).unbreakable(true).build());
    }, armorStand -> {
        armorStand.setChestplate(XMaterial.CHAINMAIL_CHESTPLATE.parseItem());
        armorStand.teleport(armorStand.getLocation().clone().add(0, 0.5, 0));
    }),
    CHAINMAIL_BOOTS(LanguageFile.CHAINMAIL_BOOTS_POWERUP, player -> {
        boolean hasItem = player.getInventory().getBoots() != null && player.getInventory().getBoots().getType() == XMaterial.CHAINMAIL_BOOTS.parseMaterial();
        if (!hasItem)
            player.getInventory().setBoots(new HItemBuilder(XMaterial.CHAINMAIL_BOOTS.parseMaterial()).addItemFlags(ItemFlag.HIDE_UNBREAKABLE).unbreakable(true).build());
    }, armorStand -> {
        armorStand.setBoots(XMaterial.CHAINMAIL_BOOTS.parseItem());
        armorStand.teleport(armorStand.getLocation().clone().add(0, 1.5, 0));
    }),
    IRON_BOOTS(LanguageFile.IRON_BOOTS_POWERUP, player -> {
        boolean hasItem = player.getInventory().getBoots() != null && player.getInventory().getBoots().getType() == XMaterial.IRON_BOOTS.parseMaterial();
        if (!hasItem)
            player.getInventory().setBoots(new HItemBuilder(XMaterial.IRON_BOOTS.parseMaterial()).addItemFlags(ItemFlag.HIDE_UNBREAKABLE).unbreakable(true).build());
    }, armorStand -> {
        armorStand.setBoots(XMaterial.IRON_BOOTS.parseItem());
        armorStand.teleport(armorStand.getLocation().clone().add(0, 1.5, 0));
    });

    private final Property<ConfigurablePowerup> property;
    private final Consumer<Player> action;
    private final Consumer<ArmorStand> adjust;

}
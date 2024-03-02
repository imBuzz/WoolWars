package me.buzz.woolwars.game.game.match.player.classes.classes;

import com.cryptomorin.xseries.XMaterial;
import com.hakan.core.item.ItemBuilder;
import me.buzz.woolwars.api.game.match.player.player.classes.PlayableClassType;
import me.buzz.woolwars.api.game.match.player.team.TeamColor;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.lang.LanguageFile;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.player.classes.PlayableClass;
import me.buzz.woolwars.game.game.match.player.equipment.ArmorSlot;
import me.buzz.woolwars.game.game.match.player.stats.WoolMatchStats;
import me.buzz.woolwars.game.player.WoolPlayer;
import me.buzz.woolwars.game.utils.TeamUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class TankPlayableClass extends PlayableClass {

    private final static Map<ArmorSlot, ItemStack> armor = new HashMap<>();
    private final static Map<Integer, ItemStack> items = new HashMap<>();

    static {
        items.put(0, new ItemBuilder(XMaterial.WOODEN_SWORD.parseItem()).addItemFlags(ItemFlag.HIDE_UNBREAKABLE).unbreakable(true).build());
        items.put(1, new ItemBuilder(XMaterial.WOODEN_PICKAXE.parseItem()).addItemFlags(ItemFlag.HIDE_UNBREAKABLE).unbreakable(true).build());
        items.put(2, new ItemBuilder(XMaterial.SHEARS.parseItem()).addItemFlags(ItemFlag.HIDE_UNBREAKABLE).unbreakable(true).build());
        items.put(3, new ItemBuilder(XMaterial.WHITE_WOOL.parseMaterial(), 64).build());

        items.put(8, WoolWars.get().getLanguage().getProperty(LanguageFile.TANK_KEYSTONE)
                .toItemStack());

        armor.put(ArmorSlot.HELMET, new ItemStack(Material.AIR));
        armor.put(ArmorSlot.CHESTPLATE, new ItemBuilder(XMaterial.LEATHER_CHESTPLATE.parseItem()).addItemFlags(ItemFlag.HIDE_UNBREAKABLE).unbreakable(true).build());
        armor.put(ArmorSlot.LEGGINGS, new ItemBuilder(XMaterial.LEATHER_LEGGINGS.parseItem()).addItemFlags(ItemFlag.HIDE_UNBREAKABLE).unbreakable(true).build());
        armor.put(ArmorSlot.BOOTS, new ItemBuilder(XMaterial.LEATHER_BOOTS.parseItem()).addItemFlags(ItemFlag.HIDE_UNBREAKABLE).unbreakable(true).build());
    }

    public TankPlayableClass(Player player, TeamColor teamColor) {
        super(player, teamColor, PlayableClassType.TANK);
    }

    public static String getBaseLayout() {
        return "123400009";
    }

    @Override
    public void onEquip(WoolPlayer woolPlayer, WoolMatchStats stats) {
        for (PotionEffect activePotionEffect : player.getActivePotionEffects())
            player.removePotionEffect(activePotionEffect.getType());

        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().setArmorContents(null);
        player.getInventory().clear();

        TeamUtils.setAbsorptionHearts(player, 4);

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
    public void useAbility(WoolMatch match, Player player) {
        if (used) return;
        used = true;
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 2, 4));
        player.sendMessage(WoolWars.get().getLanguage().getProperty(LanguageFile.ABILITY_USED));
    }

    @Override
    public void onDequip() {
        TeamUtils.setAbsorptionHearts(player, 0);
    }

}

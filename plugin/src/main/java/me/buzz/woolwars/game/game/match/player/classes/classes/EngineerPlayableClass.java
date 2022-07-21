package me.buzz.woolwars.game.game.match.player.classes.classes;

import com.cryptomorin.xseries.XMaterial;
import com.hakan.core.item.HItemBuilder;
import me.buzz.woolwars.api.game.match.player.player.classes.PlayableClassType;
import me.buzz.woolwars.api.game.match.player.team.TeamColor;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.lang.LanguageFile;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.player.classes.PlayableClass;
import me.buzz.woolwars.game.game.match.player.equipment.ArmorSlot;
import me.buzz.woolwars.game.game.match.player.stats.WoolMatchStats;
import me.buzz.woolwars.game.game.match.task.tasks.ProtectCenterTask;
import me.buzz.woolwars.game.player.WoolPlayer;
import me.buzz.woolwars.game.utils.structures.itembuilder.PotionAndWoolItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class EngineerPlayableClass extends PlayableClass {

    private final static Map<ArmorSlot, ItemStack> armor = new HashMap<>();
    private final static Map<Integer, ItemStack> items = new HashMap<>();

    static {
        items.put(0, new HItemBuilder(XMaterial.WOODEN_SWORD.parseMaterial()).addItemFlags(ItemFlag.HIDE_UNBREAKABLE).unbreakable(true).build());
        items.put(1, new HItemBuilder(XMaterial.BOW.parseMaterial()).addItemFlags(ItemFlag.HIDE_UNBREAKABLE).unbreakable(true).build());
        items.put(2, new ItemStack(Material.ARROW, 4));
        items.put(3, new HItemBuilder(XMaterial.SHEARS.parseMaterial()).addItemFlags(ItemFlag.HIDE_UNBREAKABLE).unbreakable(true).build());

        items.put(4, new HItemBuilder(XMaterial.STONE_PICKAXE.parseMaterial()).addItemFlags(ItemFlag.HIDE_UNBREAKABLE).unbreakable(true).build());
        items.put(5, new PotionAndWoolItemBuilder(Material.POTION).potion(PotionType.REGEN, 1, true).build());
        items.put(6, new HItemBuilder(XMaterial.WHITE_WOOL.parseMaterial(), 64).build());

        items.put(8, WoolWars.get().getLanguage().getProperty(LanguageFile.ENGINEER_KEYSTONE)
                .toItemStack());

        armor.put(ArmorSlot.HELMET, new ItemStack(Material.AIR));
        armor.put(ArmorSlot.CHESTPLATE, new HItemBuilder(XMaterial.LEATHER_CHESTPLATE.parseMaterial()).addItemFlags(ItemFlag.HIDE_UNBREAKABLE).unbreakable(true).build());
        armor.put(ArmorSlot.LEGGINGS, new ItemStack(Material.AIR));
        armor.put(ArmorSlot.BOOTS, new ItemStack(Material.AIR));
    }

    public EngineerPlayableClass(Player player, TeamColor teamColor) {
        super(player, teamColor, PlayableClassType.ENGINEER);
    }

    public static String getBaseLayout() {
        return "123456709";
    }

    @Override
    public void onEquip(WoolPlayer woolPlayer, WoolMatchStats stats) {
        for (PotionEffect activePotionEffect : player.getActivePotionEffects())
            player.removePotionEffect(activePotionEffect.getType());

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
    public void useAbility(WoolMatch match, Player player) {
        if (used) return;
        if (!match.getRoundHolder().canBreakCenter) {
            player.sendMessage(WoolWars.get().getLanguage().getProperty(LanguageFile.YOU_CANNOT_USE_THIS_ABILITY_YET));
            return;
        }

        used = true;
        match.getRoundHolder().canBreakCenter = false;

        match.getRoundHolder().getTasks().put(ProtectCenterTask.ID,
                new ProtectCenterTask(match, TimeUnit.SECONDS.toMillis(5)).start());

        player.sendMessage(WoolWars.get().getLanguage().getProperty(LanguageFile.ABILITY_USED));
    }

}

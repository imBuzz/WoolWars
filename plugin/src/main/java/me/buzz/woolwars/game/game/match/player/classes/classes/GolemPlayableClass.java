package me.buzz.woolwars.game.game.match.player.classes.classes;

import me.buzz.woolwars.api.game.match.player.player.classes.PlayableClassType;
import me.buzz.woolwars.api.game.match.player.team.TeamColor;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.lang.LanguageFile;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.player.classes.PlayableClass;
import me.buzz.woolwars.game.game.match.player.equipment.ArmorSlot;
import me.buzz.woolwars.game.game.match.player.stats.WoolMatchStats;
import me.buzz.woolwars.game.player.WoolPlayer;
import me.buzz.woolwars.game.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.Map;

public class GolemPlayableClass extends PlayableClass {

    private final static Map<ArmorSlot, ItemStack> armor = new HashMap<>();
    private final static Map<Integer, ItemStack> items = new HashMap<>();

    static {
        items.put(0, new ItemBuilder(Material.STONE_SWORD).setFlags(ItemFlag.HIDE_UNBREAKABLE).setUnbreakable(true).build());
        items.put(1, new ItemBuilder(Material.WOOL, 32).build());

        items.put(8, WoolWars.get().getLanguage().getProperty(LanguageFile.GOLEM_KEYSTONE)
                .toItemStack());

        armor.put(ArmorSlot.HELMET, new ItemStack(Material.AIR));
        armor.put(ArmorSlot.CHESTPLATE, new ItemStack(Material.AIR));
        armor.put(ArmorSlot.LEGGINGS, new ItemStack(Material.AIR));
        armor.put(ArmorSlot.BOOTS, new ItemBuilder(Material.GOLD_BOOTS)
                .addEnchant(Enchantment.PROTECTION_PROJECTILE, 2)
                .setFlags(ItemFlag.HIDE_UNBREAKABLE).setUnbreakable(true)
                .build());
    }

    public GolemPlayableClass(Player player, TeamColor teamColor) {
        super(player, teamColor, PlayableClassType.GOLEM);
    }

    public static String getBaseLayout() {
        return "120000009";
    }

    @Override
    public void equip(WoolPlayer woolPlayer, WoolMatchStats stats) {
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
        used = true;

        ItemStack boots = player.getInventory().getBoots().clone();

        player.getInventory().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.GOLD_BOOTS));

        player.sendMessage(WoolWars.get().getLanguage().getProperty(LanguageFile.ABILITY_USED));

        int roundID = match.getRoundHolder().getRoundNumber();
        Bukkit.getScheduler().runTaskLater(WoolWars.get(), () -> {
            if (match.getRoundHolder().getRoundNumber() != roundID) return;

            if (match.isPlaying()) {
                if (match.getPlayerHolder().getMatchStats(player).getPlayableClass().getType() == PlayableClassType.GOLEM) {
                    if (match.getPlayerHolder().isSpectator(player)) return;

                    player.getInventory().setChestplate(null);
                    player.getInventory().setLeggings(null);
                    player.getInventory().setBoots(boots);
                }
            }
        }, 20 * 5L);
    }

    @Override
    public void reset() {
        used = false;
    }

}

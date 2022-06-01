package me.buzz.woolwars.game.game.gui;

import com.hakan.core.item.HItemBuilder;
import com.hakan.core.ui.inventory.HInventory;
import me.buzz.woolwars.api.game.match.player.player.classes.PlayableClassType;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.gui.GuiFile;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.player.stats.WoolMatchStats;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;

public class ClassSelectorGui extends HInventory {

    private final WoolMatch match;
    private final WoolMatchStats stats;

    public ClassSelectorGui(WoolMatch match, WoolMatchStats stats) {
        super("class-selector-gui-" + stats.getUuid().toString(),
                WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_TITLE),
                5,
                InventoryType.CHEST);
        this.match = match;
        this.stats = stats;
    }

    @Override
    public void onOpen(HInventory hInventory, Player player) {
        createTankClass(player);
        createAssaultClass(player);
        createArcherClass(player);
        createSwordsmanClass(player);
        createEngineerClass(player);
        createGolemClass(player);
    }

    private void createTankClass(Player player) {
        HItemBuilder itemBuilder = new HItemBuilder(WoolWars.get().getGUISettings()
                .getProperty(GuiFile.CLASS_SELECTOR_TANK).toItemStack());
        if (stats.getClassType() == PlayableClassType.TANK) itemBuilder.setFlags(ItemFlag.HIDE_ENCHANTS)
                .addEnchant(Enchantment.DURABILITY, 1);

        super.setItem(11, WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_TANK).toItemStack(), event -> {
            stats.pickClass(player, stats.getTeam().getTeamColor(), PlayableClassType.TANK);
            stats.getPlayableClass().equip(match.getPlayerHolder().getWoolPlayer(player), stats);
            createTankClass(player);
        });
    }

    private void createAssaultClass(Player player) {
        HItemBuilder itemBuilder = new HItemBuilder(WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_ASSAULT).toItemStack());
        if (stats.getClassType() == PlayableClassType.ASSAULT) itemBuilder.setFlags(ItemFlag.HIDE_ENCHANTS)
                .addEnchant(Enchantment.DURABILITY, 1);

        super.setItem(13, WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_ASSAULT).toItemStack(), event -> {
            stats.pickClass(player, stats.getTeam().getTeamColor(), PlayableClassType.ASSAULT);
            stats.getPlayableClass().equip(match.getPlayerHolder().getWoolPlayer(player), stats);
            createAssaultClass(player);
        });
    }

    private void createArcherClass(Player player) {
        HItemBuilder itemBuilder = new HItemBuilder(WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_ARCHER).toItemStack());
        if (stats.getClassType() == PlayableClassType.ARCHER) itemBuilder.setFlags(ItemFlag.HIDE_ENCHANTS)
                .addEnchant(Enchantment.DURABILITY, 1);

        super.setItem(15, WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_ARCHER).toItemStack(), event -> {
            stats.pickClass(player, stats.getTeam().getTeamColor(), PlayableClassType.ARCHER);
            stats.getPlayableClass().equip(match.getPlayerHolder().getWoolPlayer(player), stats);
            createArcherClass(player);
        });
    }

    private void createSwordsmanClass(Player player) {
        HItemBuilder itemBuilder = new HItemBuilder(WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_SWORDMAN).toItemStack());
        if (stats.getClassType() == PlayableClassType.SWORDMAN) itemBuilder.setFlags(ItemFlag.HIDE_ENCHANTS)
                .addEnchant(Enchantment.DURABILITY, 1);

        super.setItem(29, WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_SWORDMAN).toItemStack(), event -> {
            stats.pickClass(player, stats.getTeam().getTeamColor(), PlayableClassType.SWORDMAN);
            stats.getPlayableClass().equip(match.getPlayerHolder().getWoolPlayer(player), stats);
            createSwordsmanClass(player);
        });
    }

    private void createGolemClass(Player player) {
        HItemBuilder itemBuilder = new HItemBuilder(WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_GOLEM).toItemStack());
        if (stats.getClassType() == PlayableClassType.GOLEM) itemBuilder.setFlags(ItemFlag.HIDE_ENCHANTS)
                .addEnchant(Enchantment.DURABILITY, 1);

        super.setItem(31, WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_GOLEM).toItemStack(), event -> {
            stats.pickClass(player, stats.getTeam().getTeamColor(), PlayableClassType.GOLEM);
            stats.getPlayableClass().equip(match.getPlayerHolder().getWoolPlayer(player), stats);
            createGolemClass(player);
        });
    }

    private void createEngineerClass(Player player) {
        HItemBuilder itemBuilder = new HItemBuilder(WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_ENGINEER).toItemStack());
        if (stats.getClassType() == PlayableClassType.ENGINEER) itemBuilder.setFlags(ItemFlag.HIDE_ENCHANTS)
                .addEnchant(Enchantment.DURABILITY, 1);

        super.setItem(33, WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_ENGINEER).toItemStack(), event -> {
            stats.pickClass(player, stats.getTeam().getTeamColor(), PlayableClassType.ENGINEER);
            stats.getPlayableClass().equip(match.getPlayerHolder().getWoolPlayer(player), stats);
            createEngineerClass(player);
        });
    }


}

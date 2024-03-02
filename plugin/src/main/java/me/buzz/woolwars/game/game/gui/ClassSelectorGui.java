package me.buzz.woolwars.game.game.gui;

import com.hakan.core.item.ItemBuilder;
import com.hakan.core.ui.inventory.InventoryGui;
import me.buzz.woolwars.api.game.match.player.player.classes.PlayableClassType;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.gui.GuiFile;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.player.stats.WoolMatchStats;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;

public class ClassSelectorGui extends InventoryGui {

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
    public void onOpen(Player player) {
        createTankClass(player);
        createAssaultClass(player);
        createArcherClass(player);
        createSwordsmanClass(player);
        createEngineerClass(player);
        createGolemClass(player);

        super.onOpen(player);
    }

    private void createTankClass(Player player) {
        ItemBuilder itemBuilder = new ItemBuilder(WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_TANK).toItemStack());
        itemBuilder.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        if (stats.getClassType() == PlayableClassType.TANK) itemBuilder.addEnchant(Enchantment.DURABILITY, 1);

        super.setItem(11, WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_TANK).toItemStack(), event -> {
            stats.pickClass(match.getPlayerHolder().getWoolPlayer(player), stats, PlayableClassType.TANK);
            createTankClass(player);
        });
    }

    private void createAssaultClass(Player player) {
        ItemBuilder itemBuilder = new ItemBuilder(WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_ASSAULT).toItemStack());
        itemBuilder.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        if (stats.getClassType() == PlayableClassType.ASSAULT) itemBuilder.addEnchant(Enchantment.DURABILITY, 1);

        super.setItem(13, WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_ASSAULT).toItemStack(), event -> {
            stats.pickClass(match.getPlayerHolder().getWoolPlayer(player), stats, PlayableClassType.ASSAULT);
            createAssaultClass(player);
        });
    }

    private void createArcherClass(Player player) {
        ItemBuilder itemBuilder = new ItemBuilder(WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_ARCHER).toItemStack());
        itemBuilder.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        if (stats.getClassType() == PlayableClassType.ARCHER) itemBuilder.addEnchant(Enchantment.DURABILITY, 1);

        super.setItem(15, WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_ARCHER).toItemStack(), event -> {
            stats.pickClass(match.getPlayerHolder().getWoolPlayer(player), stats, PlayableClassType.ARCHER);
            createArcherClass(player);
        });
    }

    private void createSwordsmanClass(Player player) {
        ItemBuilder itemBuilder = new ItemBuilder(WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_SWORDMAN).toItemStack());
        itemBuilder.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        if (stats.getClassType() == PlayableClassType.SWORDMAN) itemBuilder.addEnchant(Enchantment.DURABILITY, 1);

        super.setItem(29, WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_SWORDMAN).toItemStack(), event -> {
            stats.pickClass(match.getPlayerHolder().getWoolPlayer(player), stats, PlayableClassType.SWORDMAN);
            createSwordsmanClass(player);
        });
    }

    private void createGolemClass(Player player) {
        ItemBuilder itemBuilder = new ItemBuilder(WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_GOLEM).toItemStack());
        itemBuilder.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        if (stats.getClassType() == PlayableClassType.GOLEM) itemBuilder.addEnchant(Enchantment.DURABILITY, 1);

        super.setItem(31, WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_GOLEM).toItemStack(), event -> {
            stats.pickClass(match.getPlayerHolder().getWoolPlayer(player), stats, PlayableClassType.GOLEM);
            createGolemClass(player);
        });
    }

    private void createEngineerClass(Player player) {
        ItemBuilder itemBuilder = new ItemBuilder(WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_ENGINEER).toItemStack());
        itemBuilder.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        if (stats.getClassType() == PlayableClassType.ENGINEER) itemBuilder.addEnchant(Enchantment.DURABILITY, 1);

        super.setItem(33, WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_ENGINEER).toItemStack(), event -> {
            stats.pickClass(match.getPlayerHolder().getWoolPlayer(player), stats, PlayableClassType.ENGINEER);
            createEngineerClass(player);
        });
    }


}

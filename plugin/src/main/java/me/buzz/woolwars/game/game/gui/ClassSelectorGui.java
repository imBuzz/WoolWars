package me.buzz.woolwars.game.game.gui;

import ch.jalu.configme.properties.Property;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import lombok.RequiredArgsConstructor;
import me.buzz.woolwars.api.game.match.player.player.classes.PlayableClassType;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.gui.GuiFile;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.player.stats.MatchStats;
import me.buzz.woolwars.game.utils.ItemBuilder;
import me.buzz.woolwars.game.utils.structures.WoolItem;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ClassSelectorGui implements InventoryProvider {

    private final static Map<Property<WoolItem>, Integer> slotsByProperty = new HashMap<>();

    static {
        slotsByProperty.put(GuiFile.CLASS_SELECTOR_TANK, 11);
        slotsByProperty.put(GuiFile.CLASS_SELECTOR_ASSAULT, 13);
        slotsByProperty.put(GuiFile.CLASS_SELECTOR_ARCHER, 15);
        slotsByProperty.put(GuiFile.CLASS_SELECTOR_SWORDMAN, 29);
        slotsByProperty.put(GuiFile.CLASS_SELECTOR_GOLEM, 31);
        slotsByProperty.put(GuiFile.CLASS_SELECTOR_ENGINEER, 33);
    }

    private final WoolMatch match;
    private final MatchStats stats;

    public static SmartInventory getInventory(WoolMatch match, MatchStats stats) {
        return SmartInventory.builder()
                .provider(new ClassSelectorGui(match, stats))
                .id("class-selector-gui")
                .size(5, 9)
                .manager(WoolWars.get().getInventoryManager())
                .title(WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_TITLE))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        createTankClass(player, contents);
        createAssaultClass(player, contents);
        createArcherClass(player, contents);
        createSwordsmanClass(player, contents);
        createEngineerClass(player, contents);
        createGolemClass(player, contents);
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private void createTankClass(Player player, InventoryContents contents) {
        ItemBuilder itemBuilder = new ItemBuilder(WoolWars.get().getGUISettings()
                .getProperty(GuiFile.CLASS_SELECTOR_TANK).toItemStack());
        if (stats.getClassType() == PlayableClassType.TANK) itemBuilder.setFlags(ItemFlag.HIDE_ENCHANTS)
                .addEnchant(Enchantment.DURABILITY, 1);

        contents.set(1, 2,
                ClickableItem.of(WoolWars.get().getGUISettings().getProperty(GuiFile.CLASS_SELECTOR_TANK).toItemStack(), event -> {
                    stats.pickClass(player, stats.getTeam().getTeamColor(), PlayableClassType.TANK);
                    stats.getPlayableClass().equip(match.getPlayerHolder().getWoolPlayer(player), stats);
                    init(player, contents);
                }));
    }

    private void createAssaultClass(Player player, InventoryContents contents) {
        ItemBuilder itemBuilder = new ItemBuilder(WoolWars.get().getGUISettings()
                .getProperty(GuiFile.CLASS_SELECTOR_ASSAULT).toItemStack());
        if (stats.getClassType() == PlayableClassType.ASSAULT) itemBuilder.setFlags(ItemFlag.HIDE_ENCHANTS)
                .addEnchant(Enchantment.DURABILITY, 1);

        contents.set(1, 4,
                ClickableItem.of(itemBuilder.build(), event -> {
                    stats.pickClass(player, stats.getTeam().getTeamColor(), PlayableClassType.ASSAULT);
                    stats.getPlayableClass().equip(match.getPlayerHolder().getWoolPlayer(player), stats);
                    init(player, contents);
                }));
    }

    private void createArcherClass(Player player, InventoryContents contents) {
        ItemBuilder itemBuilder = new ItemBuilder(WoolWars.get().getGUISettings()
                .getProperty(GuiFile.CLASS_SELECTOR_ARCHER).toItemStack());
        if (stats.getClassType() == PlayableClassType.ARCHER) itemBuilder.setFlags(ItemFlag.HIDE_ENCHANTS)
                .addEnchant(Enchantment.DURABILITY, 1);

        contents.set(1, 6,
                ClickableItem.of(itemBuilder.build(), event -> {
                    stats.pickClass(player, stats.getTeam().getTeamColor(), PlayableClassType.ARCHER);
                    stats.getPlayableClass().equip(match.getPlayerHolder().getWoolPlayer(player), stats);
                    init(player, contents);
                }));
    }

    private void createSwordsmanClass(Player player, InventoryContents contents) {
        ItemBuilder itemBuilder = new ItemBuilder(WoolWars.get().getGUISettings()
                .getProperty(GuiFile.CLASS_SELECTOR_SWORDMAN).toItemStack());
        if (stats.getClassType() == PlayableClassType.SWORDMAN) itemBuilder.setFlags(ItemFlag.HIDE_ENCHANTS)
                .addEnchant(Enchantment.DURABILITY, 1);

        contents.set(3, 2,
                ClickableItem.of(itemBuilder.build(), event -> {
                    stats.pickClass(player, stats.getTeam().getTeamColor(), PlayableClassType.SWORDMAN);
                    stats.getPlayableClass().equip(match.getPlayerHolder().getWoolPlayer(player), stats);
                    init(player, contents);
                }));
    }

    private void createGolemClass(Player player, InventoryContents contents) {
        ItemBuilder itemBuilder = new ItemBuilder(WoolWars.get().getGUISettings()
                .getProperty(GuiFile.CLASS_SELECTOR_GOLEM).toItemStack());
        if (stats.getClassType() == PlayableClassType.GOLEM) itemBuilder.setFlags(ItemFlag.HIDE_ENCHANTS)
                .addEnchant(Enchantment.DURABILITY, 1);

        contents.set(3, 4,
                ClickableItem.of(itemBuilder.build(), event -> {
                    stats.pickClass(player, stats.getTeam().getTeamColor(), PlayableClassType.GOLEM);
                    stats.getPlayableClass().equip(match.getPlayerHolder().getWoolPlayer(player), stats);
                    init(player, contents);
                }));
    }

    private void createEngineerClass(Player player, InventoryContents contents) {
        ItemBuilder itemBuilder = new ItemBuilder(WoolWars.get().getGUISettings()
                .getProperty(GuiFile.CLASS_SELECTOR_ENGINEER).toItemStack());
        if (stats.getClassType() == PlayableClassType.ENGINEER) itemBuilder.setFlags(ItemFlag.HIDE_ENCHANTS)
                .addEnchant(Enchantment.DURABILITY, 1);

        contents.set(3, 6,
                ClickableItem.of(itemBuilder.build(), event -> {
                    stats.pickClass(player, stats.getTeam().getTeamColor(), PlayableClassType.ENGINEER);
                    stats.getPlayableClass().equip(match.getPlayerHolder().getWoolPlayer(player), stats);
                    init(player, contents);
                }));
    }


}

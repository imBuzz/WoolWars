package me.buzz.woolwars.game.configuration.files.gui;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.Lists;
import me.buzz.woolwars.game.utils.structures.WoolItem;

import static ch.jalu.configme.properties.PropertyInitializer.newBeanProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class GuiFile implements SettingsHolder {

    public static final Property<String> MATCHES_MONITOR = newProperty("matches-monitor.title", "§8Matches Monitor");

    public static final Property<WoolItem> ARROW_BACK = newBeanProperty(WoolItem.class, "matches-monitor.previousPage",
            WoolItem.from(XMaterial.ARROW.name(), "§7Previous Page", Lists.newArrayList()));
    public static final Property<WoolItem> CLOSE_PAGE = newBeanProperty(WoolItem.class, "matches-monitor.close",
            WoolItem.from(XMaterial.BARRIER.name(), "§7Close", Lists.newArrayList()));
    public static final Property<WoolItem> ARROW_NEXT = newBeanProperty(WoolItem.class, "matches-monitor.nextPage",
            WoolItem.from(XMaterial.ARROW.name(), "§7Next Page", Lists.newArrayList()));

    public static final Property<WoolItem> CLASSIC_MATCH_ITEM = newBeanProperty(WoolItem.class, "matches-monitor.matchItem",
            WoolItem.from(XMaterial.IRON_BLOCK.name(), "§aID: {ID}",
                    Lists.newArrayList(
                            "§7State: {state}",
                            "",
                            "§7Players: {players-counter}",
                            "§7Spectators: {spectators-counter}",
                            "",
                            "§7Arena: {arena}",
                            "",
                            "§7Blue Score: {blue_score}",
                            "§7Red Score: {red_score}"
                    )));


    public static final Property<String> CLASS_SELECTOR_TITLE = newProperty("class-selector.title", "§8Class Selector");
    public static final Property<WoolItem> CLASS_SELECTOR_TANK = newBeanProperty(WoolItem.class, "class-selector.item.tank",
            WoolItem.from(XMaterial.IRON_BLOCK.name(), "§aTank",
                    Lists.newArrayList(
                            "§7With the highest base defenses",
                            "§7and a powerful shield and",
                            "§7recovery, this kit can control",
                            "§7middle and dominate the enemy!",
                            "",
                            "§eClick to Select!"
                    )));


    public static final Property<WoolItem> CLASS_SELECTOR_ASSAULT = newBeanProperty(WoolItem.class, "class-selector.item.assault",
            WoolItem.from(XMaterial.SHEARS.name(), "§aAssault",
                    Lists.newArrayList(
                            "§7With good tools, potions and a",
                            "§7powerful blast of TNT, secure",
                            "§7the middle for a few seconds and",
                            "§7victory is within grasp!",
                            "",
                            "§eClick to Select!"
                    )));

    public static final Property<WoolItem> CLASS_SELECTOR_ARCHER = newBeanProperty(WoolItem.class, "class-selector.item.archer",
            WoolItem.from(XMaterial.BOW.name(), "§aArcher",
                    Lists.newArrayList(
                            "§7Low in armor and weak in melee,",
                            "§7the archer has to use its",
                            "§7superior mobility to kite its",
                            "§7marks and its bow to finish the",
                            "§7fight!",
                            "",
                            "§eClick to Select!"
                    )));

    public static final Property<WoolItem> CLASS_SELECTOR_SWORDMAN = newBeanProperty(WoolItem.class, "class-selector.item.swordman",
            WoolItem.from(XMaterial.STONE_SWORD.name(), "§aSwordsman",
                    Lists.newArrayList(
                            "§7Using their powerful sword and,",
                            "§7swiftness, they can cut through",
                            "§7any enemies in his way to",
                            "§7achieve victory.",
                            "",
                            "§eClick to Select!"
                    )));

    public static final Property<WoolItem> CLASS_SELECTOR_GOLEM = newBeanProperty(WoolItem.class, "class-selector.item.golem",
            WoolItem.from(XMaterial.GOLDEN_CHESTPLATE.name(), "§aGolem",
                    Lists.newArrayList(
                            "§7Powered down, the golem lacks",
                            "§7all but a hard hitting offense,",
                            "§7but for a few seconds, the golem",
                            "§7can become the toughest entity",
                            "§7around!",
                            "",
                            "§eClick to Select!"
                    )));

    public static final Property<WoolItem> CLASS_SELECTOR_ENGINEER = newBeanProperty(WoolItem.class, "class-selector.item.engineer",
            WoolItem.from(XMaterial.REDSTONE_BLOCK.name(), "§aEngineer",
                    Lists.newArrayList(
                            "§7A great all purpose class that",
                            "§7can lock middle for several",
                            "§7seconds to give your team an",
                            "§7edge!",
                            "",
                            "§eClick to Select!"
                    )));

}

package me.buzz.woolwars.game.configuration.files;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import com.google.common.collect.Lists;
import me.buzz.woolwars.game.utils.structures.Title;
import me.buzz.woolwars.game.utils.structures.WoolItem;
import org.bukkit.Material;

import java.util.List;

import static ch.jalu.configme.properties.PropertyInitializer.*;

public class LanguageFile implements SettingsHolder {

    public static final Property<String> JOINED_MESSAGE = newProperty("match.joined", "&7{player} &ehas joined (&b{current}&e/&b{max}&e)");
    public static final Property<String> LEAVE_MESSAGE = newProperty("match.quit", "&7{player} &eleft from the game (&b{current}&e/&b{max}&e)");

    public static final Property<String> NOT_ENOUGH_PLAYER_TO_PLAY = newProperty("match.not-enough-player-to-play",
            "&cThere are not enough player to keep playing!");

    public static final Property<String> STARTING_COOLDOWN = newProperty("match.starting-cooldown", "&eThe game starts in &c{seconds} &eseconds!");

    public static final Property<Title> PRE_ROUND_TITLE = newBeanProperty(Title.class, "match.pre-round.title", new Title("&e&lPRE ROUND", "&bSelect your class!"));
    public static final Property<Title> ROUND_START_TITLE = newBeanProperty(Title.class, "match.round.start.title", new Title("&a&lROUND START", "&bRound {number}"));
    public static final Property<Title> DIED_TITLE = newBeanProperty(Title.class, "match.round.died.title", new Title("&c&lYOU DIED", "&fYou will respawn at the start of the next round!"));
    public static final Property<Title> ROUND_OVER_TITLE = newBeanProperty(Title.class, "match.round.over.title", new Title("&9{blue_team_points} &f- &c{red_team_points}", "&e&lROUND OVER"));

    public static final Property<String> ROUND_UNLOCK_CENTER_BAR = newProperty("match.round.protectCenter-bar", "&e&lCENTER UNLOCKS IN {seconds} SECONDS!");
    public static final Property<String> ROUND_CANNOT_BE_CAPTURED = newProperty("match.round.cannot-be-captured", "&cYou cannot capture the center for another {seconds} seconds!");

    //ENDED MATCH

    public static final Property<String> ENDED_STATUS_LOST = newProperty("match.ended.variables.status.lost", "&cYour team lost!");
    public static final Property<String> ENDED_STATUS_VICTORY = newProperty("match.ended.variables.status.victory", "&aYour team won!");

    public static final Property<Title> ENDED_VICTORY_TITLE = newBeanProperty(Title.class, "match.ended.victory.title", new Title("&c&lVICTORY", "&6Your team won!"));
    public static final Property<Title> ENDED_LOST_TITLE = newBeanProperty(Title.class, "match.ended.lost.title", new Title("&c&lDEFEAT", "&6Your team was defeated!"));

    public static final Property<Boolean> ENDED_RESUME_CENTERED = newProperty("match.ended.resume.centered", true);
    public static final Property<List<String>> ENDED_RESUME = newListProperty("match.ended.resume.lines", Lists.newArrayList(
            "&a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬",
            "&f&lWOOL WARS",
            "",
            "{status}",
            "",
            "&e&lMost Kills &7{top_killer_name} - {top_kills}",
            "&6&lMost Wool Placed &7{top_wool_name} - {top_wool}",
            "&c&lMost Blocks Broken &7{top_blocks_name} - {top_blocks}",
            "",
            "&a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"));


    //SCOREBOARDs

    public static final Property<List<String>> SCOREBOARD_MATCH_LOBBY = newListProperty("scoreboard.lobby", Lists.newArrayList("", "CIAO", ""));

    public static final Property<List<String>> SCOREBOARD_MATCH_WAITING = newListProperty("scoreboard.match.waiting", Lists.newArrayList(
            "   ", "&fMap: &a{map_name}", "&fPlayers: &a{current_players}/{max_players}", "  ", "&fWaiting...", " ", "&eyour.server.ip"));

    public static final Property<List<String>> SCOREBOARD_MATCH_STARTING = newListProperty("scoreboard.match.starting", Lists.newArrayList(
            "   ", "&fMap: &a{map_name}", "&fPlayers: &a{current_players}/{max_players}", "  ", "&fStarting in &a{remaning_seconds}s", " ", "&eyour.server.ip"));

    public static final Property<List<String>> SCOREBOARD_MATCH_PRE_ROUND = newListProperty("scoreboard.match.pre_round", Lists.newArrayList(
            "    ",
            "&fRound: &b{round}",
            "&fState: &e{round_type}",
            "&fMap: &a{map_name}",
            "   ",
            "&c[R] {red_team_progress} &8({red_team_points}/3)",
            "&9[B] {blue_team_progress} &8({blue_team_points}/3)",
            "  ",
            "&fTime Left: &a{time_left}",
            " ",
            "&eyour.server.ip"));

    public static final Property<List<String>> SCOREBOARD_MATCH_ROUND = newListProperty("scoreboard.match.round", Lists.newArrayList(
            "     ",
            "&fRound: &b{round}",
            "&fState: &e{round_type}",
            "    ",
            "&c[R] {red_team_progress} &8({red_team_points}/3)",
            "&9[B] {blue_team_progress} &8({blue_team_points}/3)",
            "   ",
            "&cRed Players: &f{red_team_players} &7{red_team_isYou}",
            "&9Blue Players: &f{blue_team_players} &7{blue_team_isYou}",
            "  ",
            "&fTime Left: &a{time_left}",
            " ",
            "&eyour.server.ip"));

    public static final Property<List<String>> SCOREBOARD_MATCH_ROUND_OVER = newListProperty("scoreboard.match.round-over", Lists.newArrayList(
            "    ",
            "&fRound: &b{round}",
            "&fState: &e{round_type}",
            "   ",
            "&c[R] {red_team_progress} &8({red_team_points}/3)",
            "&9[B] {blue_team_progress} &8({blue_team_points}/3)",
            "  ",
            "&fTime Left: &a{time_left}",
            " ",
            "&eyour.server.ip"));

    //VARIABLES (SCOREBOARD)

    public static final Property<String> IS_YOU = newProperty("scoreboard.variables.isYou", "(You)");
    public static final Property<String> PROGRESS_SYMBOL = newProperty("scoreboard.variables.progress_symbol", "✪");

    public static final Property<String> ROUND_WAITING = newProperty("scoreboard.variables.round.waiting", "Waiting");
    public static final Property<String> ROUND_PRE_ROUND = newProperty("scoreboard.variables.round.pre_round", "Pre Round");
    public static final Property<String> ROUND_ROUND = newProperty("scoreboard.variables.round.round", "Active Round");
    public static final Property<String> ROUND_ENDED = newProperty("scoreboard.variables.round.ended", "Round Over!");

    //ITEMS

    public static final Property<Material> KEYSTONE_MATERIAL = newBeanProperty(Material.class, "classes.keystone-material", Material.BLAZE_POWDER);
    public static final Property<WoolItem> TANK_KEYSTONE = newBeanProperty(WoolItem.class, "classes.tank.keystone",
            new WoolItem("&fKeystone Ability: &6&lGIGAHEAL",
                    Lists.newArrayList(
                            "&7Give yourself a high amount of",
                            "&7regen for 2 seconds.",
                            "",
                            "&6Press &e&lQ &6or &e&lRight Click &6to activate",
                            "",
                            "&7You can only use your keystone",
                            "&c&lONCE &7per round."
                    )));

    public static final Property<WoolItem> ARCHER_KEYSTONE = newBeanProperty(WoolItem.class, "classes.archer.keystone",
            new WoolItem("&fKeystone Ability: &6&lSTEP BACK",
                    Lists.newArrayList(
                            "&7Push yourself back instantly!",
                            "",
                            "&6Press &e&lQ &6or &e&lRight Click &6to activate",
                            "",
                            "&7You can only use your keystone",
                            "&c&lONCE &7per round."
                    )));

    public static final Property<WoolItem> SWORDMAN_KEYSTONE = newBeanProperty(WoolItem.class, "classes.swordman.keystone",
            new WoolItem("&fKeystone Ability: &6&lSPRINT",
                    Lists.newArrayList(
                            "&7Give yourself a speed boost for",
                            "&73 seconds.",
                            "",
                            "&6Press &e&lQ &6or &e&lRight Click &6to activate",
                            "",
                            "&7You can only use your keystone",
                            "&c&lONCE &7per round."
                    )));

    public static final Property<WoolItem> GOLEM_KEYSTONE = newBeanProperty(WoolItem.class, "classes.golem.keystone",
            new WoolItem("&fKeystone Ability: &6&lGOLDEN SHELL",
                    Lists.newArrayList(
                            "&7Encase yourself in Golden Armor",
                            "&7for 5 seconds.",
                            "",
                            "&6Press &e&lQ &6or &e&lRight Click &6to activate",
                            "",
                            "&7You can only use your keystone",
                            "&c&lONCE &7per round."
                    )));

    public static final Property<WoolItem> ENGINEER_KEYSTONE = newBeanProperty(WoolItem.class, "classes.engineer.keystone",
            new WoolItem("&fKeystone Ability: &6&lHACK",
                    Lists.newArrayList(
                            "&7Disable players from placing or",
                            "&7breaking middle blocks for 3",
                            "&7seconds.",
                            "",
                            "&6Press &e&lQ &6or &e&lRight Click &6to activate",
                            "",
                            "&7You can only use your keystone",
                            "&c&lONCE &7per round."
                    )));

    public static final Property<WoolItem> ASSAULT_KEYSTONE = newBeanProperty(WoolItem.class, "classes.assault.keystone",
            new WoolItem("&fKeystone Ability: &6&lKNOCKBACK TNT",
                    Lists.newArrayList(
                            "&7Place a TNT that doesn't deal",
                            "&7damage, but deals massive",
                            "&7knockback to enemies within 4",
                            "&7blocks.",
                            "",
                            "&6Press &e&lQ &6or &e&lRight Click &6to activate",
                            "",
                            "&7You can only use your keystone",
                            "&c&lONCE &7per round."
                    )));


}
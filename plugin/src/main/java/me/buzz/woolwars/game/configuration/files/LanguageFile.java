package me.buzz.woolwars.game.configuration.files;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import com.google.common.collect.Lists;

import java.util.List;

import static ch.jalu.configme.properties.PropertyInitializer.newListProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class LanguageFile implements SettingsHolder {

    public static final Property<String> JOINED_MESSAGE = newProperty("match.joined", "&7%player% &ehas joined (&b{current}&e/&b{max}&e)");
    public static final Property<String> STARTING_COOLDOWN = newProperty("match.starting-cooldown", "&eThe game starts in &c{seconds} &eseconds!");

    public static final Property<String> PRE_ROUND_TITLE = newProperty("match.pre-round.title.title", "&e&lPRE ROUND");
    public static final Property<String> PRE_ROUND_SUBTITLE = newProperty("match.pre-round.title.subtitle", "&bSelect your class!");

    public static final Property<String> ROUND_START_TITLE = newProperty("match.round.start.title.title", "&a&lROUND START");
    public static final Property<String> ROUND_START_SUBTITLE = newProperty("match.round.start.title.subtitle", "&bRound {number}");

    public static final Property<String> DIED_TITLE = newProperty("match.round.died.title.title", "&c&lYOU DIED");
    public static final Property<String> DIED_SUBTITLE = newProperty("match.round.died.title.subtitle", "&fYou will respawn at the start of the next round!");

    public static final Property<String> ROUND_OVER_TITLE = newProperty("match.round.over.title.title", "&9{blue_team_points} &f- &c{red_team_points}");
    public static final Property<String> ROUND_OVER_SUBTITLE = newProperty("match.round.over.title.subtitle", "&e&lROUND OVER");

    public static final Property<String> ROUND_UNLOCK_CENTER_BAR = newProperty("match.round.protectCenter-bar", "&e&lCENTER UNLOCKS IN {seconds} SECONDS!");

    public static final Property<String> ROUND_CANNOT_BE_CAPTURED = newProperty("match.round.cannot-be-captured", "&cYou cannot capture the center for another {seconds} seconds!");

    /*

    ENDED MATCH

     */

    public static final Property<String> ENDED_STATUS_LOST = newProperty("match.ended.variables.status.lost", "&cYour team lost!");
    public static final Property<String> ENDED_STATUS_VICTORY = newProperty("match.ended.variables.status.victory", "&aYour team won!");

    public static final Property<String> ENDED_VICTORY_TITLE = newProperty("match.ended.victory.title.title", "&c&lVICTORY");
    public static final Property<String> ENDED_VICTORY_SUBTITLE = newProperty("match.ended.victory.title.subtitle", "&6Your team won!");

    public static final Property<String> ENDED_LOST_TITLE = newProperty("match.ended.lost.title.title", "&c&lDEFEAT");
    public static final Property<String> ENDED_LOST_SUBTITLE = newProperty("match.ended.lost.title.subtitle", "&6Your team was defeated!");

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


    /*
    SCOREBOARDs
     */

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

    /*
    VARIABLES (SCOREBOARD)
     */


    public static final Property<String> IS_YOU = newProperty("scoreboard.variables.isYou", "(You)");
    public static final Property<String> PROGRESS_SYMBOL = newProperty("scoreboard.variables.progress_symbol", "✪");

    public static final Property<String> ROUND_WAITING = newProperty("scoreboard.variables.round.waiting", "Waiting");
    public static final Property<String> ROUND_STARTING = newProperty("scoreboard.variables.round.starting", "Starting");
    public static final Property<String> ROUND_PRE_ROUND = newProperty("scoreboard.variables.round.pre_round", "Pre Round");
    public static final Property<String> ROUND_ROUND = newProperty("scoreboard.variables.round.round", "Active Round");
    public static final Property<String> ROUND_ENDED = newProperty("scoreboard.variables.round.ended", "Round Over!");

}

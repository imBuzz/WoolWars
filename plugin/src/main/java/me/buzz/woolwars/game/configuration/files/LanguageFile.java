package me.buzz.woolwars.game.configuration.files;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class LanguageFile implements SettingsHolder {

    public static final Property<String> JOINED_MESSAGE = newProperty("match.joined", "&7%player% &ehas joined (&b%current%&e/&b%max%&e)");
    public static final Property<String> STARTING_COOLDOWN = newProperty("match.starting-cooldown", "&eThe game starts in %seconds% &eseconds!");

    public static final Property<String> PRE_ROUND_TITLE = newProperty("match.pre-round.title.title", "&e&lPRE ROUND");
    public static final Property<String> PRE_ROUND_SUBTITLE = newProperty("match.pre-round.title.subtitle", "&bSelect your class!");

    public static final Property<String> ROUND_START_TITLE = newProperty("match.round.start.title.title", "&a&lROUND START");
    public static final Property<String> ROUND_START_SUBTITLE = newProperty("match.round.start.title.subtitle", "&bRound %number%");

    public static final Property<String> DIED_TITLE = newProperty("match.round.died.title.title", "&c&lYOU DIED");
    public static final Property<String> DIED_SUBTITLE = newProperty("match.round.died.title.subtitle", "&fYou will respawn at the start of the next round!");

    public static final Property<String> ROUND_OVER_TITLE = newProperty("match.round.over.title.title", "&9%blue_team_points% &f- &c%red_team_points%");
    public static final Property<String> ROUND_OVER_SUBTITLE = newProperty("match.round.over.title.subtitle", "&e&lROUND OVER");

}

package me.buzz.woolwars.game.configuration.files;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class ConfigFile implements SettingsHolder {

    public static final Property<Integer> START_COOLDOWN = newProperty("match.start-cooldown-seconds", 5);
    public static final Property<Integer> PRE_ROUND_TIMER = newProperty("match.pre-round-timer-seconds", 12);
    public static final Property<Integer> ROUND_DURATION = newProperty("match.round-duration-seconds", 60);
    public static final Property<Integer> CENTER_UNLOCKS_COOLDOWN = newProperty("match.center-unlocks-seconds", 10);
    public static final Property<Integer> WAIT_FOR_NEW_ROUND_TIMER = newProperty("match.wait-for-new-round", 5);

}

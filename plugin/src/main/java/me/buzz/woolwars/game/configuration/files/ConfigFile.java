package me.buzz.woolwars.game.configuration.files;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import me.buzz.woolwars.game.game.arena.location.SerializedLocation;

import static ch.jalu.configme.properties.PropertyInitializer.newBeanProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class ConfigFile implements SettingsHolder {

    public static final Property<SerializedLocation> LOBBY_LOCATION = newBeanProperty(SerializedLocation.class, "match.locations.lobby",
            SerializedLocation.from("world", 0, 100, 0, 0, 0));

    public static final Property<Integer> START_COOLDOWN = newProperty("match.start-cooldown-seconds", 5);
    public static final Property<Integer> PRE_ROUND_TIMER = newProperty("match.pre-round-timer-seconds", 12);
    public static final Property<Integer> ROUND_DURATION = newProperty("match.round-duration-seconds", 60);
    public static final Property<Integer> CENTER_UNLOCKS_COOLDOWN = newProperty("match.center-unlocks-seconds", 10);
    public static final Property<Integer> WAIT_FOR_NEW_ROUND_TIMER = newProperty("match.wait-for-new-round", 5);
    public static final Property<Integer> CLOSE_GAME_COOLDOWN = newProperty("match.close-game-cooldown", 5);

}

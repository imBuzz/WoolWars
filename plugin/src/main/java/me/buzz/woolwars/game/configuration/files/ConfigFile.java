package me.buzz.woolwars.game.configuration.files;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import me.buzz.woolwars.game.data.DataProviderType;
import me.buzz.woolwars.game.data.credentials.DatabaseCredentials;
import me.buzz.woolwars.game.game.arena.location.SerializedLocation;

import java.util.List;

import static ch.jalu.configme.properties.PropertyInitializer.newBeanProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newListProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class ConfigFile implements SettingsHolder {

    public static final Property<SerializedLocation> LOBBY_LOCATION = newBeanProperty(SerializedLocation.class, "match.locations.lobby",
            SerializedLocation.from("world", 0, 100, 0, 0, 0));

    public static final Property<Integer> START_COOLDOWN = newProperty("match.starting-cooldown-duration-seconds", 5);
    public static final Property<Integer> PRE_ROUND_TIMER = newProperty("match.pre-round-duration-seconds", 12);
    public static final Property<Integer> ROUND_DURATION = newProperty("match.round-duration-seconds", 60);
    public static final Property<Integer> CENTER_UNLOCKS_COOLDOWN = newProperty("match.center-unlocks-seconds", 10);
    public static final Property<Integer> WAIT_FOR_NEW_ROUND_TIMER = newProperty("match.wait-for-new-round", 5);
    public static final Property<Integer> CLOSE_GAME_COOLDOWN = newProperty("match.close-game-cooldown", 5);

    public static final Property<Boolean> ENABLE_LEAVE_COMMAND = newProperty("match.options.enable-leave-command", true);
    public static final Property<Boolean> ENABLE_NATIVE_TABLIST = newProperty("match.options.enable-native-tablist", true);
    public static final Property<Boolean> ENABLE_NATIVE_SCOREBOARD = newProperty("match.options.enable-native-scoreboard", true);
    public static final Property<Boolean> ENABLE_FALL_DAMAGE = newProperty("match.options.enable-fall-damage", false);
    public static final Property<List<String>> DISABLED_INTERACTION_BLOCKS = newListProperty("match.options.disabled-interaction-blocks",
            "CRAFTING_TABLE", "FURNACE");

    public static final Property<XMaterial> JUMP_MATERIAL = newBeanProperty(XMaterial.class, "match.jump-pads.material", XMaterial.SLIME_BLOCK);
    public static final Property<Double> JUMP_HORIZONTAL_POWER = newProperty("match.jump-pads.power.horizontal", 0.5);
    public static final Property<Double> JUMP_VERTICAL_POWER = newProperty("match.jump-pads.power.vertical", 1.0);

    public static final Property<Double> CLASSES_ARCHER_POWER = newProperty("match.classes.archer.ability.stepbackPower", -4.0);
    public static final Property<Double> CLASSES_ASSAULT_POWER = newProperty("match.classes.assault.ability.tntPower", -5.0);

    @Comment("Set this to true to make the list as whitelist.")
    public static final Property<Boolean> AS_WHITELIST_BLOCKED_COMMANDS = newProperty("blocked-commands.whitelist", false);
    @Comment("All players with the permission woolwars.ignore-commands-block will ignore this feature")
    public static final Property<List<String>> BLOCKED_COMMANDS_LIST = newListProperty("blocked-commands.commands", "/spawn");

    public static final Property<XSound> SOUNDS_JUMP_PAD = newBeanProperty(XSound.class, "sounds.jump-pad", XSound.ENTITY_BAT_TAKEOFF);
    public static final Property<XSound> SOUNDS_ROUND_START = newBeanProperty(XSound.class, "sounds.round-start", XSound.BLOCK_ANVIL_LAND);
    public static final Property<XSound> SOUNDS_ROUND_WON = newBeanProperty(XSound.class, "sounds.round-won", XSound.ENTITY_PLAYER_LEVELUP);
    public static final Property<XSound> SOUNDS_ROUND_LOST = newBeanProperty(XSound.class, "sounds.round-lost", XSound.ENTITY_GHAST_WARN);
    public static final Property<XSound> SOUNDS_TELEPORT = newBeanProperty(XSound.class, "sounds.teleport", XSound.ENTITY_ENDERMAN_TELEPORT);
    public static final Property<XSound> SOUNDS_GAME_WON = newBeanProperty(XSound.class, "sounds.game-won", XSound.ENTITY_PLAYER_LEVELUP);
    public static final Property<XSound> SOUNDS_GAME_LOST = newBeanProperty(XSound.class, "sounds.game-lost", XSound.ENTITY_GHAST_WARN);
    public static final Property<XSound> SOUNDS_POWERUP_COLLECTED = newBeanProperty(XSound.class, "sounds.powerup-collected", XSound.ENTITY_EXPERIENCE_ORB_PICKUP);
    public static final Property<XSound> SOUNDS_PLAYER_DEATH = newBeanProperty(XSound.class, "sounds.player-death", XSound.ENTITY_GHAST_WARN);
    public static final Property<XSound> SOUNDS_PLAYER_KILL = newBeanProperty(XSound.class, "sounds.player-kill", XSound.ENTITY_EXPERIENCE_ORB_PICKUP);
    public static final Property<XSound> SOUNDS_COOLDOWN = newBeanProperty(XSound.class, "sounds.cooldown", XSound.ENTITY_CHICKEN_EGG);
    public static final Property<XSound> SOUNDS_PLAYER_JOINED = newBeanProperty(XSound.class, "sounds.player-joined", XSound.ENTITY_CHICKEN_EGG);
    public static final Property<XSound> SOUNDS_PLAYER_LEFT = newBeanProperty(XSound.class, "sounds.player-left", XSound.ENTITY_CHICKEN_EGG);

    @Comment("Choose one between MYSQL or SQLITE")
    public static final Property<DataProviderType> DATABASE_TYPE = newBeanProperty(DataProviderType.class, "database.type", DataProviderType.SQLITE);
    public static final Property<DatabaseCredentials> DATABASE_CREDENTIALS = newBeanProperty(DatabaseCredentials.class, "database.credentials",
            DatabaseCredentials.from("localhost", 3306, "minecraft", "root", "root"));

    @Override
    public void registerComments(CommentsConfiguration conf) {
        conf.setComment("", "", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~", "       WOOLWARS CONFIGURATION FILE         ", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~", "");
        conf.setComment("blocked-commands", "", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~", "       COMMANDS SETTINGS         ", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~", "");
        conf.setComment("sounds", "", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~", "       SOUNDS SETTINGS         ", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~", "");
        conf.setComment("database", "", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~", "       DATABASE SETTINGS         ", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~", "");
    }

}

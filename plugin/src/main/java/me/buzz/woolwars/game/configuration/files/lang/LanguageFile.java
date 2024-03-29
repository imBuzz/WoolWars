package me.buzz.woolwars.game.configuration.files.lang;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.Lists;
import me.buzz.woolwars.game.game.match.entities.powerup.ConfigurablePowerup;
import me.buzz.woolwars.game.utils.structures.Title;
import me.buzz.woolwars.game.utils.structures.WoolItem;

import java.util.List;

import static ch.jalu.configme.properties.PropertyInitializer.newBeanProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newListProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class LanguageFile implements SettingsHolder {

    public static final Property<String> COMMANDS_NO_PERMISSION = newProperty("commands.general.no-permission", "§cYou don't have the permission to do that!");
    public static final Property<String> RELOAD_COMPLETED = newProperty("commands.reload-completed", "§aReload completed!");
    public static final Property<String> INVALID_ARGS = newProperty("commands.general.invalid-args", "§cInvalid Args!");
    public static final Property<String> TARGET_NOT_FOUND = newProperty("commands.general.target-not-found", "§cTarget not found!");
    public static final Property<String> NO_GAME_FOUND_ON_COMMAND = newProperty("commands.general.no-game-found-on-command", "§cNo game found!");
    public static final Property<String> COMMAND_NOT_ENABLED = newProperty("commands.general.command-not-enabled", "§cThis command is not enabled!");

    public static final Property<String> CANNOT_EXECUTE_BY_THAT_ENTITY = newProperty("commands.general.wrong-entity", "§cThis entity cannot perform that command!");
    public static final Property<String> NO_MATCH = newProperty("commands.general.no-match-for-player", "§cYou have to be in a match to do that!");

    public static final Property<String> YOUR_ARE_IN_A_MATCH = newProperty("commands.join.already-in-match", "§cYou are already in a match");
    public static final Property<String> NO_MATCH_FOUND = newProperty("commands.join.no-match-found", "§cThere isn't a free match to join");

    @Comment("Global chat format")
    public static final Property<String> LOBBY_CHAT = newProperty("global.chat.lobby.format", "{prefix}{player}: {message}");

    public static final Property<String> YOU_LEFT_FROM_THE_GAME = newProperty("match.you-left-from-the-game", "§cYou left from the game");
    public static final Property<String> NOT_ENOUGH_PLAYER_TO_PLAY = newProperty("match.not-enough-player-to-play", "§cThere are not enough player to keep playing!");
    public static final Property<String> STARTING_COOLDOWN = newProperty("match.starting-cooldown", "§eThe game starts in §c{seconds} §eseconds!");
    public static final Property<String> STARTING_FAILED = newProperty("match.starting-failed", "§cStarting cancelled for not enough players!");

    public static final Property<String> ROUND_3_COOLDOWN_CHAT = newProperty("match.started-round.cooldown.3.chat", "§e3");
    public static final Property<String> ROUND_2_COOLDOWN_CHAT = newProperty("match.started-round.cooldown.2.chat", "§e2");
    public static final Property<String> ROUND_1_COOLDOWN_CHAT = newProperty("match.started-round.cooldown.1.chat", "§e1");
    public static final Property<String> ROUND_STARTED_CHAT = newProperty("match.started-round.chat", "§eRound Started!");

    public static final Property<Title> ROUND_3_COOLDOWN_TITLE = newBeanProperty(Title.class, "match.started-round.cooldown.3.title",
            new Title("§e3", ""));
    public static final Property<Title> ROUND_2_COOLDOWN_TITLE = newBeanProperty(Title.class, "match.started-round.cooldown.2.title",
            new Title("§e2", ""));
    public static final Property<Title> ROUND_1_COOLDOWN_TITLE = newBeanProperty(Title.class, "match.started-round.cooldown.1.title",
            new Title("§e1", ""));

    public static final Property<Title> PRE_ROUND_TITLE = newBeanProperty(Title.class, "match.pre-round.title", new Title("§e§lPRE ROUND", "§bSelect your class!"));
    public static final Property<Title> ROUND_START_TITLE = newBeanProperty(Title.class, "match.round.start.title", new Title("§a§lROUND START", "§bRound {number}"));
    public static final Property<Title> DIED_TITLE = newBeanProperty(Title.class, "match.round.died.title", new Title("§c§lYOU DIED", "§fYou will respawn at the start of the next round!"));
    public static final Property<Title> ROUND_OVER_TITLE = newBeanProperty(Title.class, "match.round.over.title", new Title("§9{blue_team_points} §f- §c{red_team_points}", "§e§lROUND OVER"));

    public static final Property<String> CENTER_UNLOCK = newProperty("match.round.center-unlocked", "§a§lCENTER UNLOCKED!");
    public static final Property<String> ROUND_UNLOCK_CENTER_BAR = newProperty("match.round.protectCenter-bar", "§e§lCENTER UNLOCKS IN {seconds} SECONDS!");
    public static final Property<String> ROUND_CANNOT_BE_CAPTURED = newProperty("match.round.cannot-be-captured", "§cYou cannot capture the center for another {seconds} seconds!");

    public static final Property<String> TEN_SECONDS_REMAINING = newProperty("match.round.ten-seconds-remaining",
            "§c§l10 §fseconds left in the round!");

    //KILL
    public static final Property<String> ACTIONBAR_ON_ATTACK = newProperty("match.kills.actionbar-on-hit",
            "{victimTeamColor}{victim} §r{healthBar}");

    public static final Property<String> DIED = newProperty("match.kills.died", "{victimTeamColor}{victim} §7died");
    public static final Property<String> DIED_FROM_LAVA = newProperty("match.kills.died_source.lava", "{victimTeamColor}{victim} §7died from Lava");
    public static final Property<String> DIED_FROM_VOID = newProperty("match.kills.died_source.void", "{victimTeamColor}{victim} §7died from The Void");
    public static final Property<String> KILL_BY_SOMEONE = newProperty("match.kills.killedBySomeone", "{victimTeamColor}{victim} §7was killed by {killerTeamColor}{killer}.");

    //ENDED MATCH

    public static final Property<String> ENDED_STATUS_LOST = newProperty("match.ended.variables.status.lost", "§cYour team lost!");
    public static final Property<String> ENDED_STATUS_VICTORY = newProperty("match.ended.variables.status.victory", "§aYour team won!");

    public static final Property<Title> ENDED_VICTORY_TITLE = newBeanProperty(Title.class, "match.ended.victory.title", new Title("§c§lVICTORY", "§6Your team won!"));
    public static final Property<Title> ENDED_LOST_TITLE = newBeanProperty(Title.class, "match.ended.lost.title", new Title("§c§lDEFEAT", "§6Your team was defeated!"));

    public static final Property<List<String>> MATCH_START_INFORMATION = newListProperty("match.started.resume", Lists.newArrayList(
            "§a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬",
            "",
            "§e§lWELCOME TO WOOLWARS",
            "",
            "§a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"));

    public static final Property<Boolean> ENDED_RESUME_CENTERED = newProperty("match.ended.resume.centered", true);
    public static final Property<List<String>> ENDED_RESUME = newListProperty("match.ended.resume.lines", Lists.newArrayList(
            "§a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬",
            "§f§lWOOL WARS",
            "",
            "{status}",
            "",
            "§e§lMost Kills §7{top_killer_name} - {top_kills}",
            "§6§lMost Wool Placed §7{top_wool_name} - {top_wool}",
            "§c§lMost Blocks Broken §7{top_blocks_name} - {top_blocks}",
            "",
            "§a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"));

    //POWERUPS

    public static final Property<ConfigurablePowerup> STONE_PICKAXE_POWERUP =
            newBeanProperty(ConfigurablePowerup.class, "match.powerups.stone-pickaxe",
                    ConfigurablePowerup.from("§6Stone Pickaxe", "§aYou got a Stone Pickaxe"));

    public static final Property<ConfigurablePowerup> STONE_SWORD_POWERUP =
            newBeanProperty(ConfigurablePowerup.class, "match.powerups.stone-sword",
                    ConfigurablePowerup.from("§6Stone Sword", "§aYou got a Stone Sword"));

    public static final Property<ConfigurablePowerup> BOW_POWERUP =
            newBeanProperty(ConfigurablePowerup.class, "match.powerups.bow",
                    ConfigurablePowerup.from("§6Bow", "§aYou got a Bow"));

    public static final Property<ConfigurablePowerup> CHAINMAIL_HELMET_POWERUP =
            newBeanProperty(ConfigurablePowerup.class, "match.powerups.chainmail-helmet",
                    ConfigurablePowerup.from("§6Chainmail Helmet", "§aYou got a Chainmail Helmet"));

    public static final Property<ConfigurablePowerup> CHAINMAIL_CHESTPLATE_POWERUP =
            newBeanProperty(ConfigurablePowerup.class, "match.powerups.chainmail-chestplate",
                    ConfigurablePowerup.from("§6Chainmail Chestplate", "§aYou got a Chainmail Chestplate"));

    public static final Property<ConfigurablePowerup> CHAINMAIL_BOOTS_POWERUP =
            newBeanProperty(ConfigurablePowerup.class, "match.powerups.chainmail-boots",
                    ConfigurablePowerup.from("§6Chainmail Boots", "§aYou got a Chainmail Boots"));

    public static final Property<ConfigurablePowerup> IRON_BOOTS_POWERUP =
            newBeanProperty(ConfigurablePowerup.class, "match.powerups.iron-boots",
                    ConfigurablePowerup.from("§6Iron Boots", "§aYou got a Iron Boots"));

    public static final Property<ConfigurablePowerup> INSTANT_HEAL_POWERUP =
            newBeanProperty(ConfigurablePowerup.class, "match.powerups.instant-heal",
                    ConfigurablePowerup.from("§6Instant Heal", "§aYou got healed"));
    public static final Property<ConfigurablePowerup> STRENGTH_POWERUP =
            newBeanProperty(ConfigurablePowerup.class, "match.powerups.strength-boost",
                    ConfigurablePowerup.from("§6Strength Boost", "§aYou got Strength Boost"));
    public static final Property<ConfigurablePowerup> SPEED_POWERUP =
            newBeanProperty(ConfigurablePowerup.class, "match.powerups.speed-boost",
                    ConfigurablePowerup.from("§6Speed Boost", "§aYou got Speed Boost"));
    public static final Property<ConfigurablePowerup> JUMP_BOOST_POWERUP =
            newBeanProperty(ConfigurablePowerup.class, "match.powerups.jump-boost",
                    ConfigurablePowerup.from("§6Jump Boost", "§aYou got Jump Boost"));

    //NPCS
    public static final Property<List<String>> NPC_NAME = newListProperty("match.npc.hologram", Lists.newArrayList(
            "§6§lClass Selector", "§eClick to open!"));

    public static final Property<String> SKIN_TEXTURE = newProperty("match.npc.skin.texture", "ewogICJ0aW1lc3RhbXAiIDogMTY1MjczMDU4MjQ2MSwKICAicHJvZmlsZU" +
            "lkIiA6ICI3ZWQ2ZTE1NzE4ZTc0NTA3ODdkNjgwMjA5ZTIxZWM0MSIsCiAgInByb2ZpbGVOYW1lIiA6ICJnNGczcyIsCiAgIn" +
            "NpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3Rle" +
            "HR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yMTQ0ZDY5Mjc5Zjc2YjY0ZmMxNWJmOWZkMmRlZWExNWYyNWYzYTM5OThmMGZiNDcwNT" +
            "QwYmQyYzE2NjdlYTM1IgogICAgfQogIH0KfQ==");
    public static final Property<String> SKIN_SIGNATURE = newProperty("match.npc.skin.signature", "fhTdzMgh/8IgzaIl46+LN4JpYfuxtXjia5anECpTPcxXvuoFyNteY5pzEI0" +
            "v1G0PlrKSYmJcR83HyT+DpRyBFcthVzigZzaPGUjEkkY6nEhaHhnGMvI8moKpdEKf+qg5zYHlQlRNOwlfxlaSZPzNqY1Mw+aiUj/Z" +
            "/SypkdI3jdhIdnxarg1MzZfbN+eo+FgkTJlf1hrUz6P9Cl56dXBDBM1MugXBhUZAEto2zHriV56acOMORQubuGLtMtGYi" +
            "TB1RbDY6SY/F9zgaOdBetyWhwfWrWx9/KuK3D10qzpFMT6X6EnaN7Jv65Y8PDy97zCfXzYp8RcvsRAKQIi+erG+x4cbcU7SDSR4pW0+" +
            "ZENhiOxvi4/lPH45ngCYciz+5S2CTjMK+WCpPQeYg5Dbbe0LGzpE0vLOxxp9Mb64vfa3IFyeqqXyRXKIKLaZG2HHgjaNQTa/oZIdIgtTs1Ez7cXfvfLoVoq7G8yeAFyVhg" +
            "gd09EZewVV+huxRrMfKbgRjpf6uMvoyo6PDBKU/9HX1wOJwovuHPbs+dqvJ5h/NdytAhRGvMKc7EU8iyC1Elv/GsXBf8mTGOZezunjEa+LYNIQsCLdMR3SVPDuF/11/8" +
            "s1tRqvjJ2tOcR5YbPjDzhvoO0G/5KWX9mRJFOxPLRx4x3HEO8ZvsMdPos8FWL3bumaSgk=");

    //SCOREBOARDs

    public static final Property<String> SCOREBOARD_TITLE = newProperty("scoreboard.title", "§e§lWOOL WARS");
    public static final Property<List<String>> SCOREBOARD_MATCH_LOBBY = newListProperty("scoreboard.lobby",
            Lists.newArrayList(
                    "First Line",
                    "Second Line",
                    "Third Line",
                    "Fourth Line",
                    " "));

    //VARIABLES (SCOREBOARD)

    public static final Property<String> IS_YOU = newProperty("scoreboard.variables.isYou", "(You)");
    public static final Property<String> PROGRESS_SYMBOL = newProperty("scoreboard.variables.progress_symbol", "✪");

    public static final Property<String> ROUND_WAITING = newProperty("scoreboard.variables.round.waiting", "Waiting");
    public static final Property<String> ROUND_PRE_ROUND = newProperty("scoreboard.variables.round.pre_round", "Pre Round");
    public static final Property<String> ROUND_ROUND = newProperty("scoreboard.variables.round.round", "Active Round");
    public static final Property<String> ROUND_ENDED = newProperty("scoreboard.variables.round.ended", "Round Over!");

    //ITEMS

    @Comment("Used when a player use tries to use an ability too soon for example: When an engineer try to use his ability while the middle area is in cooldown")
    public static final Property<String> YOU_CANNOT_USE_THIS_ABILITY_YET = newProperty("classes.ability.cannot-use-yet", "§cYou can't use this ability yet");
    public static final Property<String> ABILITY_USED = newProperty("classes.ability.used", "§aAbility Used");
    public static final Property<String> ABILITY_ALREADY_USED = newProperty("classes.ability.used-message", "§cYou already used this ability for this round");

    @Comment("Do not change the material on this section")
    public static final Property<WoolItem> RETURN_TO_LOBBY = newBeanProperty(WoolItem.class, "items.lobby",
            WoolItem.from(XMaterial.RED_BED.name(), "§c§lReturn To Lobby §7(Right Click)",
                    Lists.newArrayList(
                            "§7Right-click to leave to the lobby!"
                    ), 8));

    @Comment("Do not change the material on this section")
    public static final Property<WoolItem> TANK_KEYSTONE = newBeanProperty(WoolItem.class, "classes.tank.keystone",
            WoolItem.from(XMaterial.BLAZE_POWDER.name(), "§fKeystone Ability: §6§lGIGAHEAL",
                    Lists.newArrayList(
                            "§7Give yourself a high amount of",
                            "§7regen for 2 seconds.",
                            "",
                            "§6Press §e§lQ §6or §e§lRight Click §6to activate",
                            "",
                            "§7You can only use your keystone",
                            "§c§lONCE §7per round."
                    )));

    @Comment("Do not change the material on this section")
    public static final Property<WoolItem> ARCHER_KEYSTONE = newBeanProperty(WoolItem.class, "classes.archer.keystone",
            WoolItem.from(XMaterial.BLAZE_POWDER.name(), "§fKeystone Ability: §6§lSTEP BACK",
                    Lists.newArrayList(
                            "§7Push yourself back instantly!",
                            "",
                            "§6Press §e§lQ §6or §e§lRight Click §6to activate",
                            "",
                            "§7You can only use your keystone",
                            "§c§lONCE §7per round."
                    )));

    @Comment("Do not change the material on this section")
    public static final Property<WoolItem> SWORDMAN_KEYSTONE = newBeanProperty(WoolItem.class, "classes.swordman.keystone",
            WoolItem.from(XMaterial.BLAZE_POWDER.name(), "§fKeystone Ability: §6§lSPRINT",
                    Lists.newArrayList(
                            "§7Give yourself a speed boost for",
                            "§73 seconds.",
                            "",
                            "§6Press §e§lQ §6or §e§lRight Click §6to activate",
                            "",
                            "§7You can only use your keystone",
                            "§c§lONCE §7per round."
                    )));

    @Comment("Do not change the material on this section")
    public static final Property<WoolItem> GOLEM_KEYSTONE = newBeanProperty(WoolItem.class, "classes.golem.keystone",
            WoolItem.from(XMaterial.BLAZE_POWDER.name(), "§fKeystone Ability: §6§lGOLDEN SHELL",
                    Lists.newArrayList(
                            "§7Encase yourself in Golden Armor",
                            "§7for 5 seconds.",
                            "",
                            "§6Press §e§lQ §6or §e§lRight Click §6to activate",
                            "",
                            "§7You can only use your keystone",
                            "§c§lONCE §7per round."
                    )));

    @Comment("Do not change the material on this section")
    public static final Property<WoolItem> ENGINEER_KEYSTONE = newBeanProperty(WoolItem.class, "classes.engineer.keystone",
            WoolItem.from(XMaterial.BLAZE_POWDER.name(), "§fKeystone Ability: §6§lHACK",
                    Lists.newArrayList(
                            "§7Disable players from placing or",
                            "§7breaking middle blocks for 3",
                            "§7seconds.",
                            "",
                            "§6Press §e§lQ §6or §e§lRight Click §6to activate",
                            "",
                            "§7You can only use your keystone",
                            "§c§lONCE §7per round."
                    )));

    @Comment("Do not change the material on this section")
    public static final Property<WoolItem> ASSAULT_KEYSTONE = newBeanProperty(WoolItem.class, "classes.assault.keystone",
            WoolItem.from(XMaterial.BLAZE_POWDER.name(), "§fKeystone Ability: §6§lKNOCKBACK TNT",
                    Lists.newArrayList(
                            "§7Place a TNT that doesn't deal",
                            "§7damage, but deals massive",
                            "§7knockback to enemies within 4",
                            "§7blocks.",
                            "",
                            "§6Press §e§lQ §6or §e§lRight Click §6to activate",
                            "",
                            "§7You can only use your keystone",
                            "§c§lONCE §7per round."
                    )));


}

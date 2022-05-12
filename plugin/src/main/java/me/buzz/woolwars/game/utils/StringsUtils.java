package me.buzz.woolwars.game.utils;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class StringsUtils {

    private StringsUtils() {
        throw new AssertionError("Nope");
    }

    public static String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> colorize(List<String> strings) {
        return strings.stream().map(StringsUtils::colorize).collect(Collectors.toList());
    }

    public static List<String> colorize(String... strings) {
        return Arrays.stream(strings).map(StringsUtils::colorize).collect(Collectors.toList());
    }

}

package me.buzz.woolwars.api.game.match.player.team;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

@RequiredArgsConstructor
@Getter
public enum TeamColor {

    RED(ChatColor.RED, DyeColor.RED, 5, "R", "red-team"),
    BLUE(ChatColor.BLUE, DyeColor.BLUE, 10, "B", "blue-team");

    private final ChatColor CC;
    private final DyeColor DC;
    private final int priority;
    private final String tag;
    private final String entityTag;

    public static TeamColor fromDyeColor(DyeColor dyeColor) {
        for (TeamColor value : values()) {
            if (value.getDC() == dyeColor) return value;
        }
        return null;
    }

}

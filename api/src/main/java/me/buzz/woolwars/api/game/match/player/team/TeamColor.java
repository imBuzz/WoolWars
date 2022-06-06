package me.buzz.woolwars.api.game.match.player.team;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

@RequiredArgsConstructor
@Getter
public enum TeamColor {

    RED(ChatColor.RED, DyeColor.RED, 5, "R"),
    BLUE(ChatColor.BLUE, DyeColor.BLUE, 10, "B");

    private final ChatColor CC;
    private final DyeColor DC;
    private final int priority;
    private final String tag;

}

package me.buzz.woolwars.game.game.match.player.team.color;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

@RequiredArgsConstructor
@Getter
public enum TeamColor {

    RED(ChatColor.RED, DyeColor.RED, 5),
    BLUE(ChatColor.BLUE, DyeColor.BLUE, 10);

    private final ChatColor CC;
    private final DyeColor DC;
    private final int priority;

}

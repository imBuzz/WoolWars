package me.buzz.woolwars.game.commands;

import com.google.common.collect.Lists;
import me.buzz.woolwars.game.WoolWars;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface WoolCommand {

    default boolean hasPermission(CommandSender sender) {
        return true;
    }

    String getPermission();

    String getDescription();

    String getUsage();

    boolean onCommand(WoolWars woolWars, CommandSender sender, String[] args);

    default List<String> tabCompleter(WoolWars woolWars, CommandSender sender) {
        return Lists.newArrayList();
    }

}

package me.buzz.woolwars.game.commands.impl;

import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.commands.WoolCommand;
import org.bukkit.command.CommandSender;

public class InfoCommand implements WoolCommand {

    @Override
    public String getPermission() {
        return "woolwars.info";
    }

    @Override
    public String getDescription() {
        return "Plugin description";
    }

    @Override
    public String getUsage() {
        return "/ww info";
    }

    @Override
    public boolean onCommand(WoolWars woolWars, CommandSender sender, String[] args) {
        sender.sendMessage("");
        sender.sendMessage("  §e§l• §7You're running §e§lWOOLWARS " + woolWars.getDescription().getVersion() + " §7by §e" + woolWars.getDescription().getAuthors().get(0));
        sender.sendMessage("");
        return false;
    }

}

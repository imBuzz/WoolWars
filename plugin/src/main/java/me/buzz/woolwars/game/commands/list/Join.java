package me.buzz.woolwars.game.commands.list;

import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.commands.WoolCommand;
import me.buzz.woolwars.game.configuration.files.lang.LanguageFile;
import me.buzz.woolwars.game.player.WoolPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Join implements WoolCommand, CommandExecutor {

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(getPermission());
    }

    @Override
    public String getPermission() {
        return "woolwars.join";
    }

    @Override
    public String getDescription() {
        return "Join a game";
    }

    @Override
    public String getUsage() {
        return "/wl join";
    }

    @Override
    public boolean onCommand(WoolWars woolWars, CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (woolWars.getGameManager().getInternalMatchByPlayer(player) != null) {
            player.sendMessage(WoolWars.get().getLanguage().getProperty(LanguageFile.YOUR_ARE_IN_A_MATCH));
            return false;
        }

        if (!woolWars.getGameManager().sendToFreeGame(WoolPlayer.getWoolPlayer(player))) {
            player.sendMessage(WoolWars.get().getLanguage().getProperty(LanguageFile.NO_MATCH));
            return false;
        }

        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (WoolWars.get().getGameManager().getInternalMatchByPlayer(player) != null) {
            player.sendMessage(WoolWars.get().getLanguage().getProperty(LanguageFile.YOUR_ARE_IN_A_MATCH));
            return false;
        }

        if (!WoolWars.get().getGameManager().sendToFreeGame(WoolPlayer.getWoolPlayer(player))) {
            player.sendMessage(WoolWars.get().getLanguage().getProperty(LanguageFile.NO_MATCH));
            return false;
        }

        return true;
    }
}

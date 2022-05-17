package me.buzz.woolwars.game.commands.impl;

import me.buzz.woolwars.api.player.QuitGameReason;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.commands.WoolCommand;
import me.buzz.woolwars.game.configuration.files.lang.LanguageFile;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.player.WoolPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements WoolCommand {

    @Override
    public String getPermission() {
        return "woolwars.leave";
    }

    @Override
    public String getDescription() {
        return "Leave of the current game";
    }

    @Override
    public String getUsage() {
        return "/ww leave";
    }

    @Override
    public boolean onCommand(WoolWars woolWars, CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            WoolMatch match = woolWars.getGameManager().getInternalMatchByPlayer(player);
            if (match == null) {
                player.sendMessage(woolWars.getLanguage().getProperty(LanguageFile.NO_MATCH));
                return false;
            } else {
                match.quit(WoolPlayer.getWoolPlayer(player), QuitGameReason.OTHER);
                return true;
            }
        } else {
            sender.sendMessage(woolWars.getLanguage().getProperty(LanguageFile.CANNOT_EXECUTE_BY_THAT_ENTITY));
            return false;
        }
    }

}

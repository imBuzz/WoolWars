package me.buzz.woolwars.game.commands.impl;

import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.commands.WoolCommand;
import me.buzz.woolwars.game.configuration.files.lang.LanguageFile;
import me.buzz.woolwars.game.player.WoolPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EmptyCommand implements WoolCommand {

    @Override
    public String getPermission() {
        return "woolwars.join";
    }

    @Override
    public String getDescription() {
        return "Join into a free game";
    }

    @Override
    public String getUsage() {
        return "/ww join";
    }

    @Override
    public boolean onCommand(WoolWars woolWars, CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            WoolPlayer woolPlayer = WoolPlayer.getWoolPlayer(player);

            if (!woolWars.getGameManager().sendToFreeGame(woolPlayer)) {
                player.sendMessage(woolWars.getLanguage().getProperty(LanguageFile.NO_MATCH_FOUND));
                return false;
            }
        } else {
            sender.sendMessage(woolWars.getLanguage().getProperty(LanguageFile.CANNOT_EXECUTE_BY_THAT_ENTITY));
            return false;
        }

        return true;
    }

}

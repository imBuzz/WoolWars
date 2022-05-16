package me.buzz.woolwars.game.commands.list;

import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.commands.WoolCommand;
import me.buzz.woolwars.game.configuration.files.LanguageFile;
import me.buzz.woolwars.game.player.WoolPlayer;
import me.buzz.woolwars.game.utils.StringsUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Join implements WoolCommand {

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
            player.sendMessage(StringsUtils.colorize(WoolWars.get().getLanguage().getProperty(LanguageFile.YOUR_ARE_IN_A_MATCH)));
            return false;
        }

        if (!woolWars.getGameManager().sendToFreeGame(WoolPlayer.getWoolPlayer(player))) {
            player.sendMessage(StringsUtils.colorize(WoolWars.get().getLanguage().getProperty(LanguageFile.NO_MATCH)));
            return false;
        }

        return true;
    }
}

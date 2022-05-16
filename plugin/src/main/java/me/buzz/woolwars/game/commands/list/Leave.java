package me.buzz.woolwars.game.commands.list;

import me.buzz.woolwars.api.player.QuitGameReason;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.commands.WoolCommand;
import me.buzz.woolwars.game.configuration.files.LanguageFile;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.player.WoolPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Leave implements WoolCommand {

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(getPermission());
    }

    @Override
    public String getPermission() {
        return "woolwars.leave";
    }

    @Override
    public String getDescription() {
        return "Leave the arena";
    }

    @Override
    public String getUsage() {
        return "/wl leave";
    }

    @Override
    public boolean onCommand(WoolWars woolWars, CommandSender sender, String[] args) {
        Player player = (Player) sender;

        WoolMatch woolMatch = woolWars.getGameManager().getInternalMatchByPlayer(player);
        if (woolMatch == null) {
            player.sendMessage(WoolWars.get().getLanguage().getProperty(LanguageFile.NO_MATCH));
            return false;
        }

        woolMatch.quit(WoolPlayer.getWoolPlayer(player), QuitGameReason.OTHER);
        return true;
    }
}

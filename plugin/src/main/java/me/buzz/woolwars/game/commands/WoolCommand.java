package me.buzz.woolwars.game.commands;

import com.hakan.core.command.HCommandAdapter;
import com.hakan.core.command.executors.base.BaseCommand;
import com.hakan.core.command.executors.sub.SubCommand;
import me.buzz.woolwars.api.player.QuitGameReason;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.lang.LanguageFile;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.player.WoolPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@BaseCommand(name = "woolwars", description = "Woolwars base command", usage = "/woolwars", aliases = {"ww"})
public class WoolCommand implements HCommandAdapter {

    private final WoolWars woolWars = WoolWars.get();

    @SubCommand(permission = "woolwars.base")
    public void mainCommand(CommandSender sender, String[] args) {
        sender.sendMessage("");
        sender.sendMessage("  §e§l• §7You're running §e§lWOOLWARS " + woolWars.getDescription().getVersion() + " §7by §e" + woolWars.getDescription().getAuthors().get(0));
        sender.sendMessage("");
    }

    @SubCommand(args = {"join"}, permission = "woolwars.join")
    public void joinCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            WoolPlayer woolPlayer = WoolPlayer.getWoolPlayer(player);

            if (!woolWars.getGameManager().sendToFreeGame(woolPlayer)) {
                player.sendMessage(woolWars.getLanguage().getProperty(LanguageFile.NO_MATCH_FOUND));
            }
        } else {
            sender.sendMessage(woolWars.getLanguage().getProperty(LanguageFile.CANNOT_EXECUTE_BY_THAT_ENTITY));
        }
    }

    @SubCommand(args = {"leave"}, permission = "woolwars.join")
    public void leaveCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            WoolMatch match = woolWars.getGameManager().getInternalMatchByPlayer(player);
            if (match == null) {
                player.sendMessage(woolWars.getLanguage().getProperty(LanguageFile.NO_MATCH));
            } else {
                match.quit(WoolPlayer.getWoolPlayer(player), QuitGameReason.OTHER);
            }
        } else {
            sender.sendMessage(woolWars.getLanguage().getProperty(LanguageFile.CANNOT_EXECUTE_BY_THAT_ENTITY));
        }
    }

}

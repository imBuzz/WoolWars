package me.buzz.woolwars.game.commands;

import com.hakan.core.command.HCommandAdapter;
import com.hakan.core.command.executors.base.BaseCommand;
import com.hakan.core.command.executors.sub.SubCommand;
import me.buzz.woolwars.api.player.QuitGameReason;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.lang.LanguageFile;
import me.buzz.woolwars.game.game.gui.GameMonitorGui;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.player.WoolPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@BaseCommand(name = "woolwars", description = "Woolwars base command", usage = "/woolwars", aliases = {"ww"})
public class WoolCommand implements HCommandAdapter {

    private final WoolWars woolWars = WoolWars.get();

    @SubCommand()
    public void mainCommand(CommandSender sender, String[] args) {
        sender.sendMessage("");
        sender.sendMessage("  §e§l• §7You're running §e§lWOOLWARS " + woolWars.getDescription().getVersion() + " §7by §e" + woolWars.getDescription().getAuthors().get(0));
        sender.sendMessage("");
    }

    @SubCommand(args = {"gui"})
    public void guiCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (!sender.hasPermission("woolwars.gui")) {
                sender.sendMessage(woolWars.getLanguage().getProperty(LanguageFile.COMMANDS_NO_PERMISSION));
                return;
            }

            Player player = (Player) sender;
            new GameMonitorGui().open(player);
        } else {
            sender.sendMessage(woolWars.getLanguage().getProperty(LanguageFile.CANNOT_EXECUTE_BY_THAT_ENTITY));
        }
    }

    @SubCommand(args = {"spectate"})
    public void spectateCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (!sender.hasPermission("woolwars.spectate")) {
                sender.sendMessage(woolWars.getLanguage().getProperty(LanguageFile.COMMANDS_NO_PERMISSION));
                return;
            }

            Player player = (Player) sender;

            if (args.length != 1) {
                sender.sendMessage(woolWars.getLanguage().getProperty(LanguageFile.INVALID_ARGS));
                return;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(woolWars.getLanguage().getProperty(LanguageFile.TARGET_NOT_FOUND));
                return;
            }

            WoolMatch match = woolWars.getGameManager().getInternalMatchByPlayer(target);
            if (match != null) {
                match.joinAsSpectator(WoolPlayer.getWoolPlayer(player));
            } else {
                player.sendMessage(woolWars.getLanguage().getProperty(LanguageFile.NO_GAME_FOUND_ON_COMMAND));
            }

        } else {
            sender.sendMessage(woolWars.getLanguage().getProperty(LanguageFile.CANNOT_EXECUTE_BY_THAT_ENTITY));
        }
    }

    @SubCommand(args = {"join"})
    public void joinCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (!sender.hasPermission("woolwars.join")) {
                sender.sendMessage(woolWars.getLanguage().getProperty(LanguageFile.COMMANDS_NO_PERMISSION));
                return;
            }

            Player player = (Player) sender;

            WoolPlayer woolPlayer = WoolPlayer.getWoolPlayer(player);
            WoolMatch match = woolWars.getGameManager().getInternalMatchByPlayer(player);
            if (match != null) {
                player.sendMessage(woolWars.getLanguage().getProperty(LanguageFile.YOUR_ARE_IN_A_MATCH));
            } else if (!woolWars.getGameManager().sendToFreeGame(woolPlayer)) {
                player.sendMessage(woolWars.getLanguage().getProperty(LanguageFile.NO_MATCH_FOUND));
            }
        } else {
            sender.sendMessage(woolWars.getLanguage().getProperty(LanguageFile.CANNOT_EXECUTE_BY_THAT_ENTITY));
        }
    }

    @SubCommand(args = {"leave"})
    public void leaveCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (!sender.hasPermission("woolwars.leave")) {
                sender.sendMessage(woolWars.getLanguage().getProperty(LanguageFile.COMMANDS_NO_PERMISSION));
                return;
            }

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

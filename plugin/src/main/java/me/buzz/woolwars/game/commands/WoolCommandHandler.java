package me.buzz.woolwars.game.commands;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.commands.impl.EmptyCommand;
import me.buzz.woolwars.game.commands.impl.InfoCommand;
import me.buzz.woolwars.game.commands.impl.JoinCommand;
import me.buzz.woolwars.game.commands.impl.LeaveCommand;
import me.buzz.woolwars.game.configuration.files.lang.LanguageFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.Map;

public class WoolCommandHandler implements CommandExecutor, TabCompleter {

    @Getter
    private final Map<String, WoolCommand> commands = Maps.newHashMap();
    private final WoolWars woolWars = WoolWars.get();

    public WoolCommandHandler() {

        commands.put("woolwars", new EmptyCommand());
        commands.put("join", new JoinCommand());
        commands.put("leave", new LeaveCommand());
        commands.put("info", new InfoCommand());

        woolWars.getCommand("woolwars").setExecutor(this);
        woolWars.getCommand("woolwars").setTabCompleter(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            if (!commands.containsKey(command.getName().toLowerCase())) return false;

            WoolCommand woolCommand = commands.get(command.getName().toLowerCase());
            if (!woolCommand.hasPermission(sender)) {
                sender.sendMessage(woolWars.getLanguage().getProperty(LanguageFile.COMMANDS_NO_PERMISSION));
                return false;
            }
            woolCommand.onCommand(woolWars, sender, args);

            return true;
        }
        if (!commands.containsKey(args[0])) return false;

        WoolCommand woolCommand = commands.get(args[0]);
        if (!woolCommand.hasPermission(sender)) {
            sender.sendMessage(woolWars.getLanguage().getProperty(LanguageFile.COMMANDS_NO_PERMISSION));
            return false;
        }
        woolCommand.onCommand(woolWars, sender, args);
        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!commands.containsKey(args[0])) {
            List<String> strings = Lists.newArrayList();
            commands.forEach((key, value) -> {
                if (value.hasPermission(sender)) strings.add(key);
            });
            return strings;
        }
        WoolCommand woolCommand = commands.get(args[0]);
        return woolCommand.tabCompleter(woolWars, sender);
    }
}

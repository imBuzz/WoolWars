package me.buzz.woolwars.game.commands;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.commands.list.Join;
import me.buzz.woolwars.game.configuration.files.LanguageFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WoolCommandsHandler implements CommandExecutor, TabCompleter {

    @Getter
    private final Map<String, WoolCommand> commands = Maps.newHashMap();
    private final WoolWars woolWars = WoolWars.get();

    public WoolCommandsHandler() {

        //commands.put("join", new Join());
        //commands.put("leave", new Leave());

        woolWars.getCommand("woolwars").setExecutor(new Join());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            if (!commands.containsKey(command.getName().toLowerCase(Locale.ROOT))) return false;

            WoolCommand petsCommand = commands.get(command.getName().toLowerCase(Locale.ROOT));
            if (!petsCommand.hasPermission(sender)) {
                sender.sendMessage(WoolWars.get().getLanguage().getProperty(LanguageFile.COMMANDS_NO_PERMISSION));
                return false;
            }
            petsCommand.onCommand(woolWars, sender, args);
            return true;
        }
        if (!commands.containsKey(args[0])) return false;

        WoolCommand petsCommand = commands.get(args[0]);
        if (!petsCommand.hasPermission(sender)) {
            sender.sendMessage(WoolWars.get().getLanguage().getProperty(LanguageFile.COMMANDS_NO_PERMISSION));
            return false;
        }
        petsCommand.onCommand(woolWars, sender, args);

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
        WoolCommand petsCommand = commands.get(args[0]);
        return petsCommand.tabCompleter(woolWars, sender);
    }
}

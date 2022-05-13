package me.buzz.woolwars.game.commands;

import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.game.GameManager;
import me.buzz.woolwars.game.player.WoolPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {

    private final GameManager gameManager = WoolWars.get().getGameManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!gameManager.sendToFreeGame(new WoolPlayer(player))) {
                player.sendMessage("NO MATCHES");
            }
        }

        return false;
    }


}

package me.buzz.woolwars.game.commands;

import me.buzz.woolwars.api.game.match.state.MatchState;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.game.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    private final GameManager gameManager = WoolWars.get().getGameManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String gameID = player.getMetadata("wl-playing-game").get(0).asString();

            gameManager.getInternalMatch(gameID).setMatchState(MatchState.STARTING);
        }

        return false;
    }


}

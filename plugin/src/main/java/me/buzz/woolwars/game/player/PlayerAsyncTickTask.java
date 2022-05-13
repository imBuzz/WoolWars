package me.buzz.woolwars.game.player;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.game.GameManager;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.utils.StringsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerAsyncTickTask extends BukkitRunnable {

    private final GameManager gameManager = WoolWars.get().getGameManager();

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            BPlayerBoard board = Netherboard.instance().getBoard(player);
            if (board == null) return;

            WoolMatch match = gameManager.getInternalMatchByPlayer(player);
            if (match == null) {
                board.setAll(
                        "",
                        System.currentTimeMillis() + "",
                        "",
                        "play.testing.it"
                );
            } else {
                switch (match.getMatchState()) {
                    case WAITING: {
                        board.setAll(
                                "",
                                StringsUtils.colorize("&fMap: &a" + match.getArena().getName()),
                                StringsUtils.colorize("&fPlayers: &a" + match.getPlayerHolder().getPlayersCount() + "/" + match.getMaxPlayers()),
                                "",
                                StringsUtils.colorize("&fWaiting..."),
                                "",
                                "play.testing.it"
                        );
                        break;
                    }
                    case STARTING: {
                        board.setAll(
                                "",
                                StringsUtils.colorize("&fMap: &a" + match.getArena().getName()),
                                StringsUtils.colorize("&fPlayers: &a" + match.getPlayerHolder().getPlayersCount() + "/" + match.getMaxPlayers()),
                                "",
                                StringsUtils.colorize("&fStarting..."),
                                "",
                                "play.testing.it"
                        );
                        break;
                    }
                    case PRE_ROUND:
                    case ROUND: {
                        board.setAll(
                                "",
                                StringsUtils.colorize("&fRound: &b" + match.getRoundHolder().getRoundNumber()),
                                StringsUtils.colorize("&fState: &e" + match.getMatchState().name()),
                                StringsUtils.colorize("&fMap: &a" + match.getArena().getName()),
                                "",
                                "play.testing.it"
                        );
                        break;
                    }
                }
            }
        }
    }

}

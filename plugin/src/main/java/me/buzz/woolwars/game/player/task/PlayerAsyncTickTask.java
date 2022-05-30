package me.buzz.woolwars.game.player.task;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.game.GameManager;
import me.buzz.woolwars.game.game.arena.settings.preset.ApplicablePreset;
import me.buzz.woolwars.game.game.arena.settings.preset.PresetType;
import me.buzz.woolwars.game.game.match.WoolMatch;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerAsyncTickTask extends BukkitRunnable {

    private final GameManager gameManager = WoolWars.get().getGameManager();

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            BPlayerBoard board = Netherboard.instance().getBoard(player);
            if (board == null) return;

            WoolMatch match = gameManager.getInternalMatchByPlayer(player);
            applyScoreboardLines(getScoreboardLinesByMatchState(player, match), board);
        }
    }

    public List<String> getScoreboardLinesByMatchState(Player player, WoolMatch match) {
        try {
            return ((ApplicablePreset<List<String>, WoolMatch, Player, Void>) match.getPlayableArena().getPreset(PresetType.SCOREBOARD)).apply(match, player, null);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private void applyScoreboardLines(List<String> lines, BPlayerBoard board) {
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            board.set(line, lines.size() - i);
        }

        Set<Integer> scores = new HashSet<>(board.getLines().keySet());
        for (int score : scores) {
            if (score <= 0 || score > lines.size()) {
                board.remove(score);
            }
        }
    }

}

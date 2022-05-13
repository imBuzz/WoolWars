package me.buzz.woolwars.game.game.match.task;

import lombok.AllArgsConstructor;
import me.buzz.woolwars.game.game.match.WoolMatch;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public class CooldownTask extends BukkitRunnable {

    private final WoolMatch match;
    private final Action action;
    private int targetSeconds;

    @Override
    public void run() {
        targetSeconds--;
        if (targetSeconds <= 0) {
            action.run();
            cancel();
            return;
        }

        for (Player player : match.getPlayerHolder().getOnlinePlayers()) {
            player.sendMessage("Cooldown: " + targetSeconds);
        }
    }

    public interface Action {
        void run();
    }

}

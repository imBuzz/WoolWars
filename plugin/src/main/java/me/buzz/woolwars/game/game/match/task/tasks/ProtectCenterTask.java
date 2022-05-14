package me.buzz.woolwars.game.game.match.task.tasks;

import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.LanguageFile;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.task.impl.TickTask;
import org.bukkit.entity.Player;

public class ProtectCenterTask extends TickTask {

    private final WoolMatch match;

    public ProtectCenterTask(WoolMatch match, long targetTime) {
        super(targetTime);
        this.match = match;
    }

    @Override
    public void run() {
        if (shouldEnd()) {
            end();
            cancel();
            return;
        }

        for (Player onlinePlayer : match.getPlayerHolder().getOnlinePlayers()) {
            WoolWars.get().getNmsHandler().getPlayerHandler()
                    .sendActionBar(onlinePlayer,
                            match.getLanguage().getProperty(LanguageFile.ROUND_UNLOCK_CENTER_BAR)
                                    .replace("{seconds}", formatSecondsAndMillis()));
        }
    }

    @Override
    public void end() {
        for (Player onlinePlayer : match.getPlayerHolder().getOnlinePlayers()) {
            WoolWars.get().getNmsHandler().getPlayerHandler().sendActionBar(onlinePlayer, "");
        }
        match.getRoundHolder().setCanBreakCenter(true);
    }
}

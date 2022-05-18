package me.buzz.woolwars.game.game.match.task.tasks;

import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.lang.LanguageFile;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.task.impl.TickTask;
import org.bukkit.entity.Player;

public class ProtectCenterTask extends TickTask {

    public final static String ID = "centerProtect";

    private final WoolMatch match;

    public ProtectCenterTask(WoolMatch match, long targetTime) {
        super(targetTime);
        this.match = match;
    }

    @Override
    public void run() {
        super.run();

        if (shouldEnd()) {
            stop();
            end();
            return;
        }

        for (Player onlinePlayer : match.getPlayerHolder().getOnlinePlayers()) {
            WoolWars.get().getNmsHandler().getPlayerHandler()
                    .sendActionBar(onlinePlayer,
                            WoolWars.get().getLanguage().getProperty(LanguageFile.ROUND_UNLOCK_CENTER_BAR)
                                    .replace("{seconds}", formatSecondsAndMillis()));
        }
    }

    @Override
    public void end() {
        match.getRoundHolder().canBreakCenter = true;
        for (Player onlinePlayer : match.getPlayerHolder().getOnlinePlayers()) {
            WoolWars.get().getNmsHandler().getPlayerHandler().sendActionBar(onlinePlayer, WoolWars.get().getLanguage().getProperty(LanguageFile.CENTER_UNLOCK));
        }

        match.getRoundHolder().getTasks().remove(getID());
    }

    @Override
    public String getID() {
        return ID;
    }
}

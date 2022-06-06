package me.buzz.woolwars.game.game.match.task.tasks;

import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.lang.LanguageFile;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.task.impl.SecondsTask;
import org.bukkit.entity.Player;

public class TimeElapsedTask extends SecondsTask {

    public static final String ID = "timeElapsed";
    private final WoolMatch match;

    public TimeElapsedTask(WoolMatch match, long targetTime) {
        super(targetTime);
        this.match = match;
    }

    @Override
    public void run() {
        super.run();

        if (getRemainingSeconds() == 10) {
            for (Player onlinePlayer : match.getPlayerHolder().getOnlinePlayers()) {
                onlinePlayer.sendMessage(WoolWars.get().getLanguage().getProperty(LanguageFile.TEN_SECONDS_REMAINING));
            }
        }

        if (shouldEnd()) {
            stop();
            end();
        }
    }

    @Override
    public void end() {
        match.getRoundHolder().endRound(null);
    }

    @Override
    public String getID() {
        return ID;
    }
}

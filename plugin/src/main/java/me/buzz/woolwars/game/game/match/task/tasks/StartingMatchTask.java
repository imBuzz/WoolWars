package me.buzz.woolwars.game.game.match.task.tasks;

import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.LanguageFile;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.task.impl.SecondsTask;
import org.bukkit.entity.Player;

public class StartingMatchTask extends SecondsTask {

    public static final String ID = "startTask";
    private final WoolMatch match;

    public StartingMatchTask(WoolMatch match, long targetTime) {
        super(targetTime);
        this.match = match;
    }

    @Override
    public void run() {
        if (shouldEnd()) {
            end();
            stop();
            return;
        }

        for (Player onlinePlayer : match.getPlayerHolder().getOnlinePlayers()) {
            onlinePlayer.sendMessage(WoolWars.get().getLanguage().getProperty(LanguageFile.STARTING_COOLDOWN)
                    .replace("{seconds}", String.valueOf(getRemainingSeconds())));
        }

        super.run();
    }

    @Override
    public void end() {
        match.prepare();
    }

    @Override
    public void stop() {
        super.stop();
        match.getRoundHolder().getTasks().remove(getID());
    }

    @Override
    public String getID() {
        return ID;
    }
}

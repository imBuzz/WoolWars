package me.buzz.woolwars.game.game.match.task.tasks;

import me.buzz.woolwars.game.configuration.files.LanguageFile;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.task.impl.SecondsTask;
import me.buzz.woolwars.game.utils.StringsUtils;
import org.bukkit.entity.Player;

public class StartingMatchTask extends SecondsTask {

    private final WoolMatch match;

    public StartingMatchTask(WoolMatch match, long targetTime) {
        super(targetTime);
        this.match = match;
    }

    @Override
    public void run() {
        for (Player onlinePlayer : match.getPlayerHolder().getOnlinePlayers()) {
            onlinePlayer.sendMessage(StringsUtils.colorize(match.getLanguage().getProperty(LanguageFile.STARTING_COOLDOWN)
                    .replace("{seconds}", String.valueOf(getRemaningSeconds()))));
        }

        if (shouldEnd()) {
            end();
            cancel();
        }
    }


    @Override
    public void end() {
        match.prepare();
    }
}

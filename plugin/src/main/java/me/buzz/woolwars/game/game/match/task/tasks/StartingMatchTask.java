package me.buzz.woolwars.game.game.match.task.tasks;

import me.buzz.woolwars.api.game.match.state.MatchState;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.lang.LanguageFile;
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
        super.run();

        if (match.getPlayerHolder().getPlayersCount() < match.getMaxPlayers()) {
            for (Player onlinePlayer : match.getPlayerHolder().getOnlinePlayers()) {
                onlinePlayer.sendMessage(WoolWars.get().getLanguage().getProperty(LanguageFile.STARTING_FAILED));
            }

            match.setMatchState(MatchState.WAITING);
            stop();
            return;
        }


        if (shouldEnd()) {
            stop();
            end();
            return;
        }

        for (Player onlinePlayer : match.getPlayerHolder().getOnlinePlayers()) {
            onlinePlayer.sendMessage(WoolWars.get().getLanguage().getProperty(LanguageFile.STARTING_COOLDOWN)
                    .replace("{seconds}", String.valueOf(getRemainingSeconds())));
        }
    }

    @Override
    public void end() {
        match.prepare();
        match.getRoundHolder().getTasks().remove(getID());
    }

    @Override
    public String getID() {
        return ID;
    }
}

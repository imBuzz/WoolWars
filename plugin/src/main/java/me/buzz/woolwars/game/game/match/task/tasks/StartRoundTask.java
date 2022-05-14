package me.buzz.woolwars.game.game.match.task.tasks;

import me.buzz.woolwars.api.game.match.state.MatchState;
import me.buzz.woolwars.game.configuration.files.LanguageFile;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.task.CooldownTask;
import me.buzz.woolwars.game.game.match.task.impl.SecondsTask;
import me.buzz.woolwars.game.utils.StringsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class StartRoundTask extends SecondsTask {

    private final WoolMatch match;

    public StartRoundTask(WoolMatch match, long targetTime) {
        super(targetTime);
        this.match = match;
    }

    @Override
    public void run() {
        if (shouldEnd()) {
            end();
            cancel();
        }
    }


    @Override
    public void end() {
        CooldownTask task = match.getRoundHolder().getTasks().get("restGame");
        if (task != null) {
            if (Bukkit.getScheduler().isCurrentlyRunning(task.getTaskId()) || Bukkit.getScheduler().isQueued(task.getTaskId())) {
                Bukkit.getScheduler().cancelTask(task.getTaskId());
            }
        }

        match.setMatchState(MatchState.ROUND);
        match.getRoundHolder().removeWalls();
        match.getRoundHolder().setCanBreakCenter(false);

        for (Player onlinePlayer : match.getPlayerHolder().getOnlinePlayers()) {
            onlinePlayer.sendTitle(
                    StringsUtils.colorize(match.getLanguage().getProperty(LanguageFile.ROUND_START_TITLE)),
                    StringsUtils.colorize(match.getLanguage().getProperty(LanguageFile.ROUND_START_SUBTITLE)
                            .replace("{number}", String.valueOf(match.getRoundHolder().getRoundNumber()))));
        }

        match.getRoundHolder().getTasks().put("centerProtect", new ProtectCenterTask(match, TimeUnit.SECONDS.toMillis(10)).start(20));
        match.getRoundHolder().getTasks().put("restGame", new WaitForNewRoundTask(match, TimeUnit.MINUTES.toMillis(1)).start(20));
    }
}

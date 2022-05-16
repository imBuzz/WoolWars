package me.buzz.woolwars.game.game.match.task.tasks;

import me.buzz.woolwars.api.game.match.state.MatchState;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.ConfigFile;
import me.buzz.woolwars.game.configuration.files.LanguageFile;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.task.CooldownTask;
import me.buzz.woolwars.game.game.match.task.impl.SecondsTask;
import me.buzz.woolwars.game.utils.structures.Title;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class StartRoundTask extends SecondsTask {

    public static final String ID = "startRound";
    private final WoolMatch match;

    public StartRoundTask(WoolMatch match, long targetTime) {
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

        super.run();
    }

    @Override
    public void end() {
        CooldownTask task = match.getRoundHolder().getTasks().get("restGame");
        if (task != null) task.stop();

        match.setMatchState(MatchState.ROUND);
        match.getRoundHolder().removeWalls();
        match.getRoundHolder().setCanBreakCenter(false);

        Title title = WoolWars.get().getLanguage().getProperty(LanguageFile.ROUND_START_TITLE);
        for (Player onlinePlayer : match.getPlayerHolder().getOnlinePlayers()) {
            onlinePlayer.sendTitle(title.getTitle(), title.getSubTitle().replace("{number}", String.valueOf(match.getRoundHolder().getRoundNumber())));
        }

        match.getRoundHolder().getTasks().put(ProtectCenterTask.ID, new ProtectCenterTask(match,
                TimeUnit.SECONDS.toMillis(WoolWars.get().getSettings().getProperty(ConfigFile.CENTER_UNLOCKS_COOLDOWN))).start());
        match.getRoundHolder().getTasks().put(WaitForNewRoundTask.ID, new WaitForNewRoundTask(match,
                TimeUnit.SECONDS.toMillis(WoolWars.get().getSettings().getProperty(ConfigFile.ROUND_DURATION))).start());
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

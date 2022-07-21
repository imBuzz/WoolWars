package me.buzz.woolwars.game.game.match.task.tasks;

import com.hakan.core.HCore;
import me.buzz.woolwars.api.game.match.state.MatchState;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.ConfigFile;
import me.buzz.woolwars.game.configuration.files.lang.LanguageFile;
import me.buzz.woolwars.game.game.match.WoolMatch;
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
        super.run();
        switch (getRemainingSeconds()) {
            case 3: {
                Title title = WoolWars.get().getLanguage().getProperty(LanguageFile.ROUND_3_COOLDOWN_TITLE);
                for (Player onlinePlayer : match.getPlayerHolder().getOnlinePlayers()) {
                    onlinePlayer.sendMessage(WoolWars.get().getLanguage().getProperty(LanguageFile.ROUND_3_COOLDOWN_CHAT));
                    HCore.sendTitle(onlinePlayer, title.getTitle(), title.getSubTitle());
                }
                break;
            }
            case 2: {
                Title title = WoolWars.get().getLanguage().getProperty(LanguageFile.ROUND_2_COOLDOWN_TITLE);
                for (Player onlinePlayer : match.getPlayerHolder().getOnlinePlayers()) {
                    onlinePlayer.sendMessage(WoolWars.get().getLanguage().getProperty(LanguageFile.ROUND_2_COOLDOWN_CHAT));
                    HCore.sendTitle(onlinePlayer, title.getTitle(), title.getSubTitle());
                }
                break;
            }
            case 1: {
                Title title = WoolWars.get().getLanguage().getProperty(LanguageFile.ROUND_1_COOLDOWN_TITLE);
                for (Player onlinePlayer : match.getPlayerHolder().getOnlinePlayers()) {
                    onlinePlayer.sendMessage(WoolWars.get().getLanguage().getProperty(LanguageFile.ROUND_1_COOLDOWN_CHAT));
                    HCore.sendTitle(onlinePlayer, title.getTitle(), title.getSubTitle());
                }
                break;
            }
            case 0: {
                for (Player onlinePlayer : match.getPlayerHolder().getOnlinePlayers()) {
                    onlinePlayer.sendMessage(WoolWars.get().getLanguage().getProperty(LanguageFile.ROUND_STARTED_CHAT));
                }
                break;
            }
            default:
                break;
        }

        if (shouldEnd()) {
            stop();
            end();
        }
    }

    @Override
    public void end() {
        match.getRoundHolder().getTasks().put(ProtectCenterTask.ID, new ProtectCenterTask(match, TimeUnit.SECONDS.toMillis(WoolWars.get().getSettings().getProperty(ConfigFile.CENTER_UNLOCKS_COOLDOWN))).start());
        match.getRoundHolder().getTasks().put(TimeElapsedTask.ID, new TimeElapsedTask(match, TimeUnit.SECONDS.toMillis(WoolWars.get().getSettings().getProperty(ConfigFile.ROUND_DURATION))).start());

        match.setMatchState(MatchState.ROUND);
        match.getRoundHolder().removeWalls();
        match.getRoundHolder().canBreakCenter = false;

        Title title = WoolWars.get().getLanguage().getProperty(LanguageFile.ROUND_START_TITLE);
        for (Player onlinePlayer : match.getPlayerHolder().getOnlinePlayers()) {
            HCore.sendTitle(onlinePlayer, title.getTitle(), title.getSubTitle().replace("{number}", String.valueOf(match.getRoundHolder().getRoundNumber())));
        }

        match.getRoundHolder().getTasks().remove(getID());
    }

    @Override
    public String getID() {
        return ID;
    }
}

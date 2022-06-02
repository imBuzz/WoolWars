package me.buzz.woolwars.game.game.match.task.tasks;

import me.buzz.woolwars.api.player.QuitGameReason;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.task.impl.SecondsTask;
import me.buzz.woolwars.game.player.WoolPlayer;

public class ResetMatchTask extends SecondsTask {

    public static final String ID = "resetMatchTask";
    private final WoolMatch match;

    public ResetMatchTask(WoolMatch match, long targetTime) {
        super(targetTime);
        this.match = match;
    }

    @Override
    public void run() {
        super.run();

        if (shouldEnd()) {
            stop();
            end();
        }
    }

    @Override
    public void end() {
        match.getRoundHolder().getTasks().remove(getID());

        for (WoolPlayer woolPlayer : match.getPlayerHolder().getWoolPlayers()) {
            match.quit(woolPlayer, QuitGameReason.GAME_ENDED);
        }
        match.reset();
    }

    @Override
    public String getID() {
        return ID;
    }
}

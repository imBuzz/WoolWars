package me.buzz.woolwars.game.game.match.task.tasks;

import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.task.impl.SecondsTask;

public class WaitForNewRoundTask extends SecondsTask {

    public static final String ID = "waitForNewRound";
    private final WoolMatch match;

    public WaitForNewRoundTask(WoolMatch match, long targetTime) {
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
        match.getRoundHolder().startNewRound();
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

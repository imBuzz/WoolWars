package me.buzz.woolwars.game.game.match.task.tasks;

import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.task.impl.SecondsTask;

public class WaitForNewRoundTask extends SecondsTask {

    public static final String ID = "waitForNewRound";
    private final WoolMatch match;

    public WaitForNewRoundTask(WoolMatch match, long targetTime) {
        super(targetTime);
        this.match = match;

        System.out.println("NEW TASK CREATED " + getClass().getSimpleName());
    }

    @Override
    public void run() {
        if (shouldEnd()) {
            super.stop();
            end();
            stop();
            return;
        }

        super.run();
    }

    @Override
    public void end() {
        System.out.println("STARTED BY " + getClass().getSimpleName());
        match.getRoundHolder().startNewRound();
    }

    @Override
    public void stop() {
        match.getRoundHolder().getTasks().remove(getID());
    }

    @Override
    public String getID() {
        return ID;
    }
}

package me.buzz.woolwars.game.game.match.task.tasks;

import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.task.impl.SecondsTask;

public class TimeElapsedTask extends SecondsTask {

    public static final String ID = "timeElapsed";
    private final WoolMatch match;

    public TimeElapsedTask(WoolMatch match, long targetTime) {
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
        System.out.println("ENDED BY " + getClass().getSimpleName());
        match.getRoundHolder().endRound(null);
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

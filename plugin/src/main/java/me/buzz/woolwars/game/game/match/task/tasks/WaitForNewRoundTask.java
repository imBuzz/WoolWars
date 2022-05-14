package me.buzz.woolwars.game.game.match.task.tasks;

import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.task.impl.SecondsTask;

public class WaitForNewRoundTask extends SecondsTask {

    private final WoolMatch match;

    public WaitForNewRoundTask(WoolMatch match, long targetTime) {
        super(targetTime);
        this.match = match;
    }

    @Override
    public void run() {
        match.getRoundHolder().startNewRound();
    }


}

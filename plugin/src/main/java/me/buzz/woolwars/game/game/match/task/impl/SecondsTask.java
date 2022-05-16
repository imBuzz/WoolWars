package me.buzz.woolwars.game.game.match.task.impl;

import me.buzz.woolwars.game.game.match.task.CooldownTask;

import java.util.concurrent.TimeUnit;

public abstract class SecondsTask extends CooldownTask {

    public SecondsTask(long targetTime) {
        super(targetTime, TimeUnit.SECONDS.toMillis(1));
    }

    public boolean shouldEnd() {
        return targetTime - (System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(1)) <= 0;
    }

}

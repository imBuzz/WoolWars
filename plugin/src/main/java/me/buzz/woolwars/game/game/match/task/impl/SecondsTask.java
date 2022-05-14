package me.buzz.woolwars.game.game.match.task.impl;

import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.game.match.task.CooldownTask;

import java.util.concurrent.TimeUnit;

public abstract class SecondsTask extends CooldownTask {

    public SecondsTask(long targetTime) {
        super(targetTime);
    }

    public boolean shouldEnd() {
        return targetTime - (System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(1)) <= 0;
    }

    @Override
    public CooldownTask start(long delay) {
        delay += 20;

        super.start(delay);
        runTaskTimer(WoolWars.get(), delay, 20L);
        return this;
    }

}

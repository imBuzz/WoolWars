package me.buzz.woolwars.game.game.match.task.impl;

import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.game.match.task.CooldownTask;

public abstract class TickTask extends CooldownTask {

    public TickTask(long targetTime) {
        super(targetTime);
    }

    public boolean shouldEnd() {
        return targetTime - System.currentTimeMillis() <= 0;
    }

    @Override
    public CooldownTask start(long delay) {
        super.start(delay);
        runTaskTimer(WoolWars.get(), delay, 1L);
        return this;
    }
}

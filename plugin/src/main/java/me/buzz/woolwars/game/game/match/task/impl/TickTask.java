package me.buzz.woolwars.game.game.match.task.impl;

import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.game.match.task.CooldownTask;

public abstract class TickTask extends CooldownTask {

    public TickTask(long targetTime) {
        super(targetTime);
    }

    @Override
    public CooldownTask start() {
        runTaskTimer(WoolWars.get(), 0L, 1L);
        return this;
    }

}

package me.buzz.woolwars.game.game.match.task.impl;

import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.game.match.task.CooldownTask;

public abstract class SecondsTask extends CooldownTask {

    public SecondsTask(long targetTime) {
        super(targetTime);
    }

    @Override
    public CooldownTask start() {
        runTaskTimer(WoolWars.get(), 0L, 20L);
        return this;
    }

}

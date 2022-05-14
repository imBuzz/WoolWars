package me.buzz.woolwars.game.game.match.task.impl;

import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.game.match.task.CooldownTask;

public abstract class SecondsTask extends CooldownTask {

    public SecondsTask(long targetTime) {
        super(targetTime);
    }

    @Override
    public CooldownTask start(long delay) {
        super.start(delay);
        runTaskTimer(WoolWars.get(), delay, 20L);
        return this;
    }

}

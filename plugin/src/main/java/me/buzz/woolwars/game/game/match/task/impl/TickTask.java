package me.buzz.woolwars.game.game.match.task.impl;

import me.buzz.woolwars.game.game.match.task.CooldownTask;

public abstract class TickTask extends CooldownTask {

    public TickTask(long targetTime) {
        super(targetTime, 50);
    }

}

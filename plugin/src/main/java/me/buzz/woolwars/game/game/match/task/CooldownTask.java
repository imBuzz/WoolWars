package me.buzz.woolwars.game.game.match.task;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public abstract class CooldownTask extends BukkitRunnable {

    private long startTime, targetTime;

    public CooldownTask(long targetTime) {
        this.targetTime = targetTime;
    }

    public CooldownTask start() {
        startTime = System.currentTimeMillis();
        targetTime += startTime;
        return this;
    }

    public boolean shouldEnd() {
        return targetTime - System.currentTimeMillis() <= 0;
    }

    public String formatSeconds() {
        return DurationFormatUtils.formatDuration(targetTime - startTime, "mm:ss");
    }

    public String formatSecondsAndMillis() {
        return String.format("%1$tS.%1$tL", targetTime - startTime);
    }

    public int getRemaningSeconds() {
        return (int) TimeUnit.MILLISECONDS.toSeconds(targetTime - startTime);
    }

    /*public static class Builder {

        private long targetTime;

        public static Builder create() {
            return new Builder();
        }

        public Builder time(long targetTime) {
            this.targetTime = targetTime;
            return this;
        }

        public CooldownTask build(CooldownTaskType type) {
            if (type == CooldownTaskType.TICK) return new TickTask(targetTime);
            return new SecondsTask(targetTime);
        }
    }*/


}

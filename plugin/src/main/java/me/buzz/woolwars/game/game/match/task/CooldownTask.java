package me.buzz.woolwars.game.game.match.task;

import me.buzz.woolwars.game.game.match.WoolMatch;
import org.apache.commons.lang.time.DurationFormatUtils;

import java.util.concurrent.TimeUnit;

public abstract class CooldownTask {

    private final long sleepTime;
    private long nextActionTime;

    protected long targetTime;

    public CooldownTask(long targetTime, long sleepTime) {
        this.targetTime = targetTime;
        this.sleepTime = sleepTime;
    }

    public CooldownTask start() {
        targetTime = System.currentTimeMillis() + targetTime;
        WoolMatch.cooldownTaskBucket.add(this);
        return this;
    }

    public boolean shouldEnd() {
        return targetTime - System.currentTimeMillis() <= 0;
    }

    public abstract void end();

    public boolean canRun() {
        return nextActionTime - System.currentTimeMillis() <= 0;
    }

    public void run() {
        nextActionTime = System.currentTimeMillis() + sleepTime;
    }

    public void stop() {
        WoolMatch.cooldownTaskBucket.remove(this);
    }

    public String formatSeconds() {
        return DurationFormatUtils.formatDuration(targetTime - System.currentTimeMillis(), "mm:ss");
    }

    public String formatSecondsAndMillis() {
        long time = targetTime - System.currentTimeMillis();
        String s = DurationFormatUtils.formatDuration(time, "ss.SSS");
        if (TimeUnit.MILLISECONDS.toSeconds(time) >= 10) return s.substring(0, s.length() - 1);
        else return s.substring(1, s.length() - 1);
    }

    public int getRemainingSeconds() {
        return (int) TimeUnit.MILLISECONDS.toSeconds(targetTime - System.currentTimeMillis());
    }

    public abstract String getID();

}

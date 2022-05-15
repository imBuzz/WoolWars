package me.buzz.woolwars.game.game.match.task;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

//TODO: QUANDO STARTA IL ROUND DATO DAL "PAREGGIO" SI SOVRAPPONE TUTTO CONTROLLARE BENE STA SITUA

public abstract class CooldownTask extends BukkitRunnable {

    protected long targetTime;

    public CooldownTask(long targetTime) {
        this.targetTime = targetTime;
    }

    public CooldownTask start(long delay) {
        targetTime = System.currentTimeMillis() + targetTime;
        return this;
    }

    public abstract boolean shouldEnd();

    public abstract void end();

    public void stop() {
        if (Bukkit.getScheduler().isCurrentlyRunning(getTaskId()) || Bukkit.getScheduler().isQueued(getTaskId())) {
            cancel();
        }
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

    public int getRemaningSeconds() {
        return (int) TimeUnit.MILLISECONDS.toSeconds(targetTime - System.currentTimeMillis());
    }

    public abstract String getID();

}

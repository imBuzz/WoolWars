package me.buzz.woolwars.game.game.match.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.buzz.woolwars.game.WoolWars;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public class CooldownTask extends BukkitRunnable {

    private Action midAction;
    private Action endAction;
    @Getter
    private int targetSeconds;

    @Override
    public void run() {
        targetSeconds--;
        if (targetSeconds <= 0) {
            if (endAction != null) endAction.run(this);
            cancel();
            return;
        }

        if (midAction != null) midAction.run(this);
    }

    public void start() {
        runTaskTimer(WoolWars.get(), 0L, 20L);
    }

    public interface Action {
        void run(CooldownTask task);
    }

    public static class Builder {

        private Action midAction;
        private Action endAction;
        private int seconds;

        public static Builder create() {
            return new Builder();
        }

        public Builder endAction(Action endAction) {
            this.endAction = endAction;
            return this;
        }

        public Builder midAction(Action midAction) {
            this.midAction = midAction;
            return this;
        }

        public Builder seconds(int seconds) {
            this.seconds = seconds;
            return this;
        }

        public CooldownTask build() {
            return new CooldownTask(midAction, endAction, seconds);
        }
    }


}

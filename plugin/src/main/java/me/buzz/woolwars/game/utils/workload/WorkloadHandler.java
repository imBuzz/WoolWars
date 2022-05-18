package me.buzz.woolwars.game.utils.workload;

import me.buzz.woolwars.game.WoolWars;
import org.bukkit.Bukkit;

import java.util.concurrent.ConcurrentLinkedDeque;

public class WorkloadHandler {

    private static final byte MAX_MS_PER_TICK = 20;
    private static final ConcurrentLinkedDeque<Workload> workloadDeque = new ConcurrentLinkedDeque<>();

    public static void addLoad(Workload workload) {
        workloadDeque.add(workload);
    }

    public static void run() {
        Bukkit.getScheduler().runTaskTimer(WoolWars.get(), () -> {
            long stopTime = System.currentTimeMillis() + MAX_MS_PER_TICK;
            while (!workloadDeque.isEmpty() && System.currentTimeMillis() <= stopTime) {
                Workload workload = workloadDeque.pollFirst();
                if (workload == null) continue;
                workload.compute();
            }
        }, 1L, 1L);
    }


}

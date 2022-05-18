package me.buzz.woolwars.game.game.match.task.tasks;

import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.ConfigFile;
import me.buzz.woolwars.game.game.arena.location.SerializedLocation;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.task.impl.SecondsTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ResetMatchTask extends SecondsTask {

    public static final String ID = "resetMatchTask";
    private final WoolMatch match;

    public ResetMatchTask(WoolMatch match, long targetTime) {
        super(targetTime);
        this.match = match;

        System.out.println("NEW TASK CREATED " + getClass().getSimpleName());
    }

    @Override
    public void run() {
        if (shouldEnd()) {
            super.stop();
            end();
            stop();
            return;
        }
        super.run();
    }

    @Override
    public void end() {
        SerializedLocation location = WoolWars.get().getSettings().getProperty(ConfigFile.LOBBY_LOCATION);
        for (Player onlinePlayer : match.getPlayerHolder().getOnlinePlayers())
            onlinePlayer.teleport(location.toBukkitLocation(Bukkit.getWorld(location.getWorldName())));
        match.reset();
    }

    @Override
    public String getID() {
        return ID;
    }
}

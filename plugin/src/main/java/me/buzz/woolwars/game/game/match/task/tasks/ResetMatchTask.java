package me.buzz.woolwars.game.game.match.task.tasks;

import me.buzz.woolwars.api.player.QuitGameReason;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.ConfigFile;
import me.buzz.woolwars.game.game.arena.location.SerializedLocation;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.task.impl.SecondsTask;
import me.buzz.woolwars.game.player.WoolPlayer;
import org.bukkit.Bukkit;

public class ResetMatchTask extends SecondsTask {

    public static final String ID = "resetMatchTask";
    private final WoolMatch match;

    public ResetMatchTask(WoolMatch match, long targetTime) {
        super(targetTime);
        this.match = match;
    }

    @Override
    public void run() {
        super.run();

        if (shouldEnd()) {
            stop();
            end();
        }
    }

    @Override
    public void end() {
        match.getRoundHolder().getTasks().remove(getID());

        SerializedLocation location = WoolWars.get().getSettings().getProperty(ConfigFile.LOBBY_LOCATION);
        for (WoolPlayer woolPlayer : match.getPlayerHolder().getWoolPlayers()) {
            match.quit(woolPlayer, QuitGameReason.GAME_ENDED);
            woolPlayer.toBukkitPlayer().teleport(location.toBukkitLocation(Bukkit.getWorld(location.getWorldName())));
        }
        match.reset();
    }

    @Override
    public String getID() {
        return ID;
    }
}

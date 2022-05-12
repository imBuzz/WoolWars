package me.buzz.woolwars.game.player;

import lombok.Getter;
import me.buzz.woolwars.api.game.match.player.player.ApiWoolPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class WoolPlayer implements ApiWoolPlayer {

    public final UUID UUID;
    private final String name;

    public int woolPlaced, blocksBroken, powerUpsGotten;
    public int wins, played, kills, deaths;

    public WoolPlayer(Player player) {
        this.UUID = player.getUniqueId();
        this.name = player.getName();
    }

    public Player toBukkitPlayer() {
        return Bukkit.getPlayer(UUID);
    }

}

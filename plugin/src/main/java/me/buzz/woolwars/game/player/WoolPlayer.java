package me.buzz.woolwars.game.player;

import lombok.Getter;
import lombok.Setter;
import me.buzz.woolwars.api.game.match.player.player.ApiWoolPlayer;
import me.buzz.woolwars.api.game.match.player.player.classes.PlayableClassType;
import me.buzz.woolwars.game.game.match.player.classes.classes.TankPlayableClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class WoolPlayer implements ApiWoolPlayer {

    public final UUID UUID;
    private final String name;

    @Setter
    private Map<PlayableClassType, String> kitLayout = new HashMap<>();

    public int woolPlaced, blocksBroken, powerUpsGotten;
    public int wins, played, kills, deaths;

    public WoolPlayer(Player player) {
        this.UUID = player.getUniqueId();
        this.name = player.getName();
    }

    public String getKitLayout(PlayableClassType type) {
        return kitLayout.get(type);
    }

    public void load() {
        kitLayout.put(PlayableClassType.TANK, TankPlayableClass.getBaseLayout());
    }

    public Player toBukkitPlayer() {
        return Bukkit.getPlayer(UUID);
    }

}

package me.buzz.woolwars.game.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.buzz.woolwars.api.game.match.player.player.ApiWoolPlayer;
import me.buzz.woolwars.api.game.match.player.player.classes.PlayableClassType;
import me.buzz.woolwars.game.game.match.player.classes.classes.*;
import me.buzz.woolwars.game.game.match.player.stats.WoolMatchStats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class WoolPlayer implements ApiWoolPlayer {

    private final static Map<String, WoolPlayer> woolPlayersByName = new HashMap<>();
    @Setter
    private boolean inMatch;

    public static WoolPlayer getWoolPlayer(Player player) {
        return woolPlayersByName.get(player.getName());
    }

    public static void trackPlayer(WoolPlayer player) {
        woolPlayersByName.put(player.getName(), player);
    }

    public static WoolPlayer removePlayer(Player player) {
        return woolPlayersByName.remove(player.getName());
    }

    private final UUID UUID;
    private final String name;

    @Setter
    private Map<PlayableClassType, String> kitLayout = new HashMap<>();

    public int woolPlaced, blocksBroken, powerUpsGotten;
    public int wins, played, kills, deaths;

    public static Collection<WoolPlayer> getWoolOnlinePlayers() {
        return woolPlayersByName.values();
    }

    public WoolPlayer(Player player) {
        this.UUID = player.getUniqueId();
        this.name = player.getName();
    }

    public String getKitLayout(PlayableClassType type) {
        return kitLayout.get(type);
    }

    public void load() {
        if (kitLayout.isEmpty()) {
            kitLayout.put(PlayableClassType.TANK, TankPlayableClass.getBaseLayout());
            kitLayout.put(PlayableClassType.ASSAULT, AssaultPlayableClass.getBaseLayout());
            kitLayout.put(PlayableClassType.ARCHER, ArcherPlayableClass.getBaseLayout());
            kitLayout.put(PlayableClassType.SWORDMAN, SwordmanPlayableClass.getBaseLayout());
            kitLayout.put(PlayableClassType.GOLEM, GolemPlayableClass.getBaseLayout());
            kitLayout.put(PlayableClassType.ENGINEER, EngineerPlayableClass.getBaseLayout());
        }
    }

    public Player toBukkitPlayer() {
        return Bukkit.getPlayer(UUID);
    }

    public void transferFrom(WoolMatchStats stats, boolean victory) {
        woolPlaced += stats.getMatchWoolPlaced();
        blocksBroken += stats.getMatchBlocksBroken();
        powerUpsGotten += stats.getMatchPowerUpsGotten();
        played++;
        if (victory) wins++;
        deaths += stats.getMatchDeaths();
    }

}

package me.buzz.woolwars.game.player;

import fr.mrmicky.fastboard.FastBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.buzz.woolwars.api.game.match.player.player.ApiWoolPlayer;
import me.buzz.woolwars.api.game.match.player.player.classes.PlayableClassType;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.game.match.player.classes.classes.ArcherPlayableClass;
import me.buzz.woolwars.game.game.match.player.classes.classes.AssaultPlayableClass;
import me.buzz.woolwars.game.game.match.player.classes.classes.EngineerPlayableClass;
import me.buzz.woolwars.game.game.match.player.classes.classes.GolemPlayableClass;
import me.buzz.woolwars.game.game.match.player.classes.classes.SwordmanPlayableClass;
import me.buzz.woolwars.game.game.match.player.classes.classes.TankPlayableClass;
import me.buzz.woolwars.game.game.match.player.stats.WoolMatchStats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class WoolPlayer implements ApiWoolPlayer {

    private final static Map<String, WoolPlayer> woolPlayersByName = new ConcurrentHashMap<>();

    public static WoolPlayer getWoolPlayer(Player player) {
        return woolPlayersByName.get(player.getName());
    }
    public static void trackPlayer(WoolPlayer player) {
        woolPlayersByName.put(player.getName(), player);
        WoolWars.get().getTabHandler().trackPlayer(player.toBukkitPlayer(), null);
    }
    public static WoolPlayer removePlayer(Player player) {
        WoolWars.get().getTabHandler().stopTrackPlayer(player);
        return woolPlayersByName.remove(player.getName());
    }

    private final UUID UUID;
    private final String name;

    @Setter
    private Map<PlayableClassType, String> kitLayout = new HashMap<>();

    public int woolPlaced, blocksBroken, powerUpsGotten;
    public int wins, played, kills, deaths;

    @Setter
    private boolean inMatch;

    @Getter @Setter private FastBoard board;

    public static Collection<WoolPlayer> getWoolOnlinePlayers() {
        return woolPlayersByName.values();
    }

    public WoolPlayer(Player player) {
        this.UUID = player.getUniqueId();
        this.name = player.getName();
    }

    public WoolPlayer(UUID uuid, String name, Map<PlayableClassType, String> kitLayout, int woolPlaced, int blocksBroken, int powerUpsGotten,
                      int wins, int played, int kills, int deaths, boolean inMatch){

        this.UUID = uuid;
        this.name = name;
        this.kitLayout = kitLayout;
        this.woolPlaced = woolPlaced;
        this.blocksBroken = blocksBroken;
        this.powerUpsGotten = powerUpsGotten;
        this.wins = wins;
        this.played = played;
        this.kills = kills;
        this.deaths = deaths;
        this.inMatch = inMatch;
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

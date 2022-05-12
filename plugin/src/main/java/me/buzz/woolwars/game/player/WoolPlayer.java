package me.buzz.woolwars.game.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.buzz.woolwars.game.game.match.player.classes.PlayableClass;
import me.buzz.woolwars.game.game.match.player.team.impl.WoolTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class WoolPlayer {

    @Getter public final UUID uuid;
    @Getter private final String name;

    public int woolPlaced, blocksBroken, powerUpsGotten;
    public int wins, played, kills, deaths;

    public WoolPlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
    }

    public Player toBukkitPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    @RequiredArgsConstructor
    public static class MatchStats {
        @Getter
        private final UUID uuid;
        @Getter
        WoolTeam team;
        @Getter
        @Setter
        PlayableClass playableClass;

        public int matchWoolPlaced, matchBlocksBroken, matchPowerUpsGotten;
        public int matchKills, matchDeaths;
    }

}

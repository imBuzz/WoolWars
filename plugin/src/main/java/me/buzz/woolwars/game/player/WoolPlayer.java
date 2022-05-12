package me.buzz.woolwars.game.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.buzz.woolwars.api.game.match.player.player.ApiMatchStats;
import me.buzz.woolwars.api.game.match.player.player.ApiWoolPlayer;
import me.buzz.woolwars.api.game.match.player.player.classes.PlayableClassType;
import me.buzz.woolwars.game.game.match.player.classes.PlayableClass;
import me.buzz.woolwars.game.game.match.player.team.impl.WoolTeam;
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

    @RequiredArgsConstructor
    @Getter
    public static class MatchStats implements ApiMatchStats {
        private final UUID uuid;
        private WoolTeam team;
        private @Setter
        PlayableClass playableClass;

        private int matchWoolPlaced, matchBlocksBroken, matchPowerUpsGotten;
        private int matchKills, matchDeaths;

        @Override
        public PlayableClassType getClassType() {
            return playableClass.getType();
        }
    }

}

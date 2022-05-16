package me.buzz.woolwars.game.game.match.player.stats;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.buzz.woolwars.api.game.match.player.player.ApiMatchStats;
import me.buzz.woolwars.api.game.match.player.player.classes.PlayableClassType;
import me.buzz.woolwars.game.game.match.player.classes.PlayableClass;
import me.buzz.woolwars.game.game.match.player.classes.classes.AssaultPlayableClass;
import me.buzz.woolwars.game.game.match.player.team.color.TeamColor;
import me.buzz.woolwars.game.game.match.player.team.impl.WoolTeam;
import org.bukkit.entity.Player;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class MatchStats implements ApiMatchStats {
    private final UUID uuid;

    @Setter
    private WoolTeam team;
    @Setter
    private PlayableClass playableClass;

    public int matchWoolPlaced, matchBlocksBroken, matchPowerUpsGotten;
    public int matchKills, matchDeaths;

    public void pickClass(Player player, TeamColor teamColor) {
        if (playableClass == null) {
            playableClass = new AssaultPlayableClass(player, teamColor);
        }
        playableClass.reset();
    }

    @Override
    public PlayableClassType getClassType() {
        return playableClass.getType();
    }
}
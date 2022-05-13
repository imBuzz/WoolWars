package me.buzz.woolwars.game.game.match.player.stats;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.buzz.woolwars.api.game.match.player.player.ApiMatchStats;
import me.buzz.woolwars.api.game.match.player.player.classes.PlayableClassType;
import me.buzz.woolwars.game.game.match.player.classes.PlayableClass;
import me.buzz.woolwars.game.game.match.player.team.impl.WoolTeam;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class MatchStats implements ApiMatchStats {
    private final UUID uuid;
    private WoolTeam team;
    @Setter
    private PlayableClass playableClass;

    public int matchWoolPlaced, matchBlocksBroken, matchPowerUpsGotten;
    public int matchKills, matchDeaths;

    @Override
    public PlayableClassType getClassType() {
        return playableClass.getType();
    }
}
package me.buzz.woolwars.api.game.match.player.player;

import me.buzz.woolwars.api.game.match.player.player.classes.PlayableClassType;
import me.buzz.woolwars.api.game.match.player.team.ApiWoolTeam;

public interface ApiWoolMatchStats {

    PlayableClassType getClassType();

    int getMatchBlocksBroken();

    int getMatchDeaths();

    int getMatchKills();

    int getMatchPowerUpsGotten();

    int getMatchWoolPlaced();

    ApiWoolTeam getTeam();

}

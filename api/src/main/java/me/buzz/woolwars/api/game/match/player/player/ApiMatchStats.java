package me.buzz.woolwars.api.game.match.player.player;

import me.buzz.woolwars.api.game.match.player.player.classes.PlayableClassType;

public interface ApiMatchStats {

    PlayableClassType getClassType();

    int getMatchBlocksBroken();

    int getMatchDeaths();

    int getMatchKills();

    int getMatchPowerUpsGotten();

    int getMatchWoolPlaced();

}

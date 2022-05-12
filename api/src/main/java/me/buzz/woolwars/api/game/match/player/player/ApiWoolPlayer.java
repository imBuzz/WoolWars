package me.buzz.woolwars.api.game.match.player.player;

import java.util.UUID;

public interface ApiWoolPlayer {

    UUID getUUID();

    String getName();

    int getBlocksBroken();

    int getDeaths();

    int getKills();

    int getPlayed();

    int getPowerUpsGotten();

    int getWins();

    int getWoolPlaced();

}

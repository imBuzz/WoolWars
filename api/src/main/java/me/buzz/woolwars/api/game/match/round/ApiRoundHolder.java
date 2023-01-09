package me.buzz.woolwars.api.game.match.round;

import me.buzz.woolwars.api.game.match.player.team.TeamColor;

public interface ApiRoundHolder {

    int getRoundNumber();

    boolean canBreakCenter();

    int getPointsByTeamColor(TeamColor teamColor);

}

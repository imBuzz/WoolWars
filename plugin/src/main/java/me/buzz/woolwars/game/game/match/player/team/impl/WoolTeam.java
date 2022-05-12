package me.buzz.woolwars.game.game.match.player.team.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.buzz.woolwars.game.game.match.player.team.MatchTeam;
import me.buzz.woolwars.game.game.match.player.team.color.TeamColor;
import me.buzz.woolwars.game.player.WoolPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor @Getter
public class WoolTeam implements MatchTeam {

    private final String gameID;
    //SERVE DAVVERO??
    private final TeamColor teamColor;
    private final Location spawnLocation;

    private final List<Player> players = new ArrayList<>(4);
    private int points;

    public void join(WoolPlayer woolPlayer){
        players.add(woolPlayer.toBukkitPlayer());
    }

    @Override
    public List<Player> getOnlinePlayers() {
        return players;
    }

    public void increasePoints(int value){
        points += value;
    }


}

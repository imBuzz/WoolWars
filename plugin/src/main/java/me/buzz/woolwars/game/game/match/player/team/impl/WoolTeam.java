package me.buzz.woolwars.game.game.match.player.team.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.game.match.player.team.MatchTeam;
import me.buzz.woolwars.game.game.match.player.team.color.TeamColor;
import me.buzz.woolwars.game.player.WoolPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor @Getter
public class WoolTeam implements MatchTeam {

    private final String gameID;
    private final TeamColor teamColor;
    private final Location spawnLocation;

    private final Map<String, WoolPlayer.MatchStats> stats = new HashMap<>();

    private int points;

    public void join(WoolPlayer woolPlayer){
        stats.put(woolPlayer.getName(), new WoolPlayer.MatchStats(woolPlayer.getUuid()));
    }

    @Override
    public List<Player> getOnlinePlayers() {
        return stats.keySet().stream().map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .filter(player -> {
                    if (!player.hasMetadata("wl-playing-game")) return false;
                    return player.getMetadata("wl-playing-game").get(0).asString().equalsIgnoreCase(gameID);
                })
                .collect(Collectors.toList());
    }

    public void increasePoints(int value){
        points += value;
    }


}

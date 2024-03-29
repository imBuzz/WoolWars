package me.buzz.woolwars.game.game.match.player.team.impl;

import com.hakan.core.npc.Npc;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.buzz.woolwars.api.game.match.player.team.TeamColor;
import me.buzz.woolwars.game.game.match.player.stats.WoolMatchStats;
import me.buzz.woolwars.game.game.match.player.team.MatchTeam;
import me.buzz.woolwars.game.player.WoolPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class WoolTeam implements MatchTeam {

    private final String gameID;
    private final TeamColor teamColor;
    private final Location spawnLocation;

    @Setter
    private Npc teamNPC;

    private final List<Player> players = new ArrayList<>(4);
    private int points;

    public void join(WoolPlayer woolPlayer, WoolMatchStats stats) {
        stats.setTeam(this);
        players.add(woolPlayer.toBukkitPlayer());
    }

    public void remove(Player player) {
        players.remove(player);
    }


    @Override
    public List<Player> getOnlinePlayers() {
        return players;
    }

    public void increasePoints(int value) {
        points += value;
    }


}

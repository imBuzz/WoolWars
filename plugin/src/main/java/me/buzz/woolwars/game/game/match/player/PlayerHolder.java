package me.buzz.woolwars.game.game.match.player;

import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.manager.AbstractHolder;
import me.buzz.woolwars.game.player.WoolPlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerHolder extends AbstractHolder {

    private final Map<String, WoolPlayer> players = new HashMap<>();
    private final Map<String, WoolPlayer.MatchStats> stats = new HashMap<>();

    public PlayerHolder(WoolMatch match) {
        super(match);
    }

    public int getPlayersCount() {
        return players.size();
    }

    public Collection<WoolPlayer> getWoolPlayers() {
        return players.values();
    }

    public Set<Player> getOnlinePlayers() {
        return players.values().stream()
                .map(WoolPlayer::toBukkitPlayer)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public void registerPlayer(WoolPlayer player) {
        player.toBukkitPlayer().setMetadata("wl-playing-game", new FixedMetadataValue(WoolWars.get(), match.getMatchID()));
        players.put(player.getName(), player);
        stats.put(player.getName(), new WoolPlayer.MatchStats(player.getUuid()));
    }

    public void removePlayer(WoolPlayer player) {
        player.toBukkitPlayer().removeMetadata("wl-playing-game", WoolWars.get());
        players.remove(player.getName());
    }

    public WoolPlayer.MatchStats getStats(Player player) {
        return getStats(player.getName());
    }

    private WoolPlayer.MatchStats getStats(String name) {
        return stats.get(name);
    }


}

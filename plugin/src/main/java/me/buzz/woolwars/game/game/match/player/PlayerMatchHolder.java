package me.buzz.woolwars.game.game.match.player;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import me.buzz.woolwars.api.game.match.player.ApiPlayerHolder;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.player.stats.WoolMatchStats;
import me.buzz.woolwars.game.manager.AbstractMatchHolder;
import me.buzz.woolwars.game.player.WoolPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PlayerMatchHolder extends AbstractMatchHolder implements ApiPlayerHolder {

    private final Map<String, WoolPlayer> players = new LinkedHashMap();
    @Getter
    private final Map<String, WoolMatchStats> stats = new HashMap<>();

    public PlayerMatchHolder(WoolMatch match) {
        super(match);
    }

    public int getPlayersCount() {
        return players.size();
    }

    public WoolPlayer getWoolPlayer(Player player) {
        return players.get(player.getName());
    }

    public void forWoolPlayers(Consumer<WoolPlayer> consumer) {
        synchronized (players.values()) {
            for (WoolPlayer value : players.values()) {
                consumer.accept(value);
            }
        }
    }

    public Collection<WoolPlayer> getWoolPlayers() {
        return players.values();
    }

    public Set<Player> getOnlinePlayers() {
        synchronized (players) {
            return players.values().stream()
                    .map(WoolPlayer::toBukkitPlayer)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        }
    }

    public void registerPlayer(WoolPlayer player) {
        Player bukkitPlayer = player.toBukkitPlayer();
        bukkitPlayer.setMetadata("wl-playing-game", new FixedMetadataValue(WoolWars.get(), match.getMatchID()));

        players.put(player.getName(), player);
        stats.put(player.getName(), new WoolMatchStats(player.getUUID()));

        for (PotionEffect activePotionEffect : bukkitPlayer.getActivePotionEffects())
            bukkitPlayer.removePotionEffect(activePotionEffect.getType());

        bukkitPlayer.setFoodLevel(20);
        bukkitPlayer.setHealth(20);
        bukkitPlayer.setGameMode(GameMode.SURVIVAL);
        bukkitPlayer.getInventory().setArmorContents(null);
        bukkitPlayer.getInventory().clear();
    }

    public void removePlayer(WoolPlayer player) {
        player.toBukkitPlayer().removeMetadata("wl-playing-game", WoolWars.get());
        players.remove(player.getName());
    }

    public void reset() {
        players.clear();
        stats.clear();
    }

    @Override
    public boolean isSpectator(Player player) {
        return player.hasMetadata("spectator");
    }

    @Override
    public void setSpectator(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        player.setMetadata("spectator", new FixedMetadataValue(WoolWars.get(), true));
        player.setAllowFlight(true);
        player.setFlying(true);

        //TODO: ADD SPECTATORS ITEM

        for (Player onlinePlayer : getOnlinePlayers()) {
            if (onlinePlayer == player) continue;
            onlinePlayer.hidePlayer(player);
        }
    }

    @Override
    public void removeSpectator(Player player) {
        player.removeMetadata("spectator", WoolWars.get());

        player.getInventory().clear();
        player.setFlying(false);
        player.setAllowFlight(false);

        for (Player onlinePlayer : getOnlinePlayers()) {
            if (onlinePlayer == player) continue;
            onlinePlayer.showPlayer(player);
        }
    }

    @Override
    public WoolMatchStats getMatchStats(Player player) {
        return getMatchStats(player.getName());
    }

    @Override
    public WoolMatchStats getMatchStats(String name) {
        return stats.get(name);
    }

    @Override
    public ImmutableSet<Player> getPlayers() {
        return ImmutableSet.copyOf(getOnlinePlayers());
    }


}

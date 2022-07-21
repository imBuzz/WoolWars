package me.buzz.woolwars.game.game.match.player;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import me.buzz.woolwars.api.game.match.player.ApiPlayerHolder;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.ConfigFile;
import me.buzz.woolwars.game.game.arena.location.SerializedLocation;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.player.stats.WoolMatchStats;
import me.buzz.woolwars.game.manager.AbstractMatchHolder;
import me.buzz.woolwars.game.player.WoolPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PlayerMatchHolder extends AbstractMatchHolder implements ApiPlayerHolder {

    private final Map<String, WoolPlayer> players = new ConcurrentHashMap<>();
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
        for (WoolPlayer value : players.values()) {
            consumer.accept(value);
        }
    }

    public Collection<WoolPlayer> getWoolPlayers() {
        return players.values();
    }

    public Set<Player> getOnlinePlayers() {
        return players.values().stream()
                .map(WoolPlayer::toBukkitPlayer)
                .filter(Objects::nonNull)
                .filter(player -> player.hasMetadata("wl-playing-game") &&
                        Objects.equals(player.getMetadata("wl-playing-game").get(0).asString(), match.getMatchID()))
                .collect(Collectors.toSet());
    }

    public Set<Player> getGamePlayers() {
        return getOnlinePlayers().stream()
                .filter(player -> !isSpectator(player))
                .collect(Collectors.toSet());
    }

    public Set<Player> getOnlineSpectators() {
        return getOnlinePlayers().stream()
                .filter(player -> player.hasMetadata("spectator") || player.hasMetadata("default-spectator"))
                .collect(Collectors.toSet());
    }

    public void registerPlayer(WoolPlayer player, boolean createStats) {
        player.setInMatch(true);

        Player bukkitPlayer = player.toBukkitPlayer();
        bukkitPlayer.setMetadata("wl-playing-game", new FixedMetadataValue(WoolWars.get(), match.getMatchID()));

        players.put(player.getName(), player);
        if (createStats) stats.put(player.getName(), new WoolMatchStats(player.getUUID()));
        WoolWars.get().getTabHandler().update(bukkitPlayer, match);

        for (PotionEffect activePotionEffect : bukkitPlayer.getActivePotionEffects())
            bukkitPlayer.removePotionEffect(activePotionEffect.getType());

        bukkitPlayer.setFoodLevel(20);
        bukkitPlayer.setHealth(20);
        bukkitPlayer.setGameMode(GameMode.SURVIVAL);
        bukkitPlayer.getInventory().setArmorContents(null);
        bukkitPlayer.getInventory().clear();
    }

    public void removePlayer(WoolPlayer player) {
        player.setInMatch(false);
        Player bukkitPlayer = player.toBukkitPlayer();

        bukkitPlayer.removeMetadata("wl-playing-game", WoolWars.get());

        if (isSpectator(bukkitPlayer)) removeSpectator(bukkitPlayer);

        players.remove(player.getName());
        WoolWars.get().getTabHandler().update(bukkitPlayer, null);

        SerializedLocation location = WoolWars.get().getSettings().getProperty(ConfigFile.LOBBY_LOCATION);

        for (PotionEffect activePotionEffect : bukkitPlayer.getActivePotionEffects())
            bukkitPlayer.removePotionEffect(activePotionEffect.getType());
        bukkitPlayer.setGameMode(GameMode.SURVIVAL);
        bukkitPlayer.setHealth(20);
        bukkitPlayer.setFoodLevel(20);
        bukkitPlayer.getInventory().clear();
        bukkitPlayer.getInventory().setArmorContents(null);

        bukkitPlayer.teleport(location.toBukkitLocation(Bukkit.getWorld(location.getWorldName())));
        WoolWars.get().getSettings().getProperty(ConfigFile.SOUNDS_TELEPORT).play(bukkitPlayer, 1, 1);
    }

    public void reset() {
        players.clear();
        stats.clear();
    }

    @Override
    public boolean isSpectator(Player player) {
        return player.hasMetadata("spectator") || player.hasMetadata("default-spectator");
    }

    @Override
    public void setSpectator(Player player) {
        setSpectator(player, false);
    }

    public void setSpectator(Player player, boolean internal) {
        if (isSpectator(player)) return;

        player.setHealth(20);
        player.setFoodLevel(20);

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        player.setMetadata(internal ? "default-spectator" : "spectator", new FixedMetadataValue(WoolWars.get(), true));
        player.setAllowFlight(true);
        player.setFlying(true);

        //TODO: ADD SPECTATORS ITEM

        WoolWars.get().getTabHandler().update(player, match);
        for (Player onlinePlayer : getOnlinePlayers()) {
            if (onlinePlayer == player) continue;
            onlinePlayer.hidePlayer(player);
        }
    }

    @Override
    public void removeSpectator(Player player) {
        player.removeMetadata("spectator", WoolWars.get());
        player.removeMetadata("default-spectator", WoolWars.get());

        player.getInventory().clear();
        player.setFlying(false);
        player.setAllowFlight(false);

        WoolWars.get().getTabHandler().update(player, match);
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
        return ImmutableSet.copyOf(getGamePlayers());
    }

    @Override
    public ImmutableSet<Player> getSpectators() {
        return ImmutableSet.copyOf(getOnlineSpectators());
    }
}

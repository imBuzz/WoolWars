package me.buzz.woolwars.game.hook.hooks.placeholderapi;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.buzz.woolwars.api.game.match.ApiMatch;
import me.buzz.woolwars.api.game.match.player.player.ApiWoolMatchStats;
import me.buzz.woolwars.api.game.match.player.team.ApiWoolTeam;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.player.WoolPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

public class WoolwarsExpansion extends PlaceholderExpansion {

    private final Cache<ApiMatch, Integer> matchOnlinePlayersCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.SECONDS).build();

    @Override
    public @NotNull String getIdentifier() {
        return "woolwars";
    }

    @Override
    public @NotNull String getAuthor() {
        return "ImBuzz";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String param) {
        if (param.split("_")[0].equalsIgnoreCase("stats")) {
            WoolPlayer woolPlayer = WoolPlayer.getWoolPlayer(player);
            switch (param) {
                case "stats_woolplaced": {
                    return String.valueOf(woolPlayer.getWoolPlaced());
                }
                case "stats_blocksbroken": {
                    return String.valueOf(woolPlayer.getBlocksBroken());
                }
                case "stats_powerupsgotten": {
                    return String.valueOf(woolPlayer.getPowerUpsGotten());
                }
                case "stats_wins": {
                    return String.valueOf(woolPlayer.getWins());
                }
                case "stats_played": {
                    return String.valueOf(woolPlayer.getPlayed());
                }
                case "stats_kills": {
                    return String.valueOf(woolPlayer.getKills());
                }
                case "stats_deaths": {
                    return String.valueOf(woolPlayer.getDeaths());
                }
                default: {
                    return null;
                }
            }
        }
        if (param.split("_")[0].equalsIgnoreCase("match")) {
            ApiMatch match = WoolWars.get().getGameManager().getInternalMatchByPlayer(player);
            if (match == null) return null;

            ApiWoolMatchStats stats = match.getPlayerHolder().getMatchStats(player);
            switch (param) {
                case "match_total_players": {
                    if (!matchOnlinePlayersCache.asMap().containsKey(match))
                        matchOnlinePlayersCache.put(match, match.getPlayerHolder().getPlayers().size());

                    return String.valueOf(matchOnlinePlayersCache.asMap().get(match));
                }
                case "match_player_team": {
                    ApiWoolTeam team = match.getPlayerHolder().getMatchStats(player).getTeam();
                    if (team == null) return null;
                    return team.getTeamColor().name();
                }
                case "match_stats_woolplaced": {
                    return String.valueOf(stats.getMatchWoolPlaced());
                }
                case "match_stats_blocksbroken": {
                    return String.valueOf(stats.getMatchBlocksBroken());
                }
                case "match_stats_powerupsgotten": {
                    return String.valueOf(stats.getMatchPowerUpsGotten());
                }
                case "match_stats_kills": {
                    return String.valueOf(stats.getMatchKills());
                }
                case "match_stats_deaths": {
                    return String.valueOf(stats.getMatchDeaths());
                }
                default: {
                    return null;
                }
            }
        }

        return null;
    }
}

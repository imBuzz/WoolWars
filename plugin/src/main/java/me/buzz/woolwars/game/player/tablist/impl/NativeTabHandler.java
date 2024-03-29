package me.buzz.woolwars.game.player.tablist.impl;

import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.player.stats.WoolMatchStats;
import me.buzz.woolwars.game.hook.ImplementedHookType;
import me.buzz.woolwars.game.hook.hooks.vault.VaultAPIHook;
import me.buzz.woolwars.game.player.tablist.ITabHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;

public class NativeTabHandler implements ITabHandler {

    private static final String UUID = generateUUID();

    private final VaultAPIHook vaultAPIHook = WoolWars.get().getHook(ImplementedHookType.VAULT);
    private final Map<String, Team> teams = new HashMap<>();

    private static String generateUUID() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            builder.append((char) (65 + Math.random() * 26));
        }
        return builder.toString();
    }

    @Override
    public void update(Player player, WoolMatch match) {
        Team team = teams.get(player.getName());
        String newTeamName = getTeamName(player, match);

        if (team == null) {
            createTeam(player, newTeamName, match);
        } else {
            if (team.getName().equals(newTeamName)) {
                updateTeam(player, team, match);
            } else {
                createTeam(player, newTeamName, match);
            }
        }

        updateDisplayName(player, match);
        updateTab(player, match);
        updatePrefixAndSuffix(player, match, newTeamName);
    }

    @Override
    public String getTeamName(Player player, WoolMatch match) {
        int priority = getPriority(player, match);

        String priorityPrefix;

        if (priority < 0) {
            priorityPrefix = "Z";
        } else {
            char letter = (char) ((priority / 5) + 65);
            int repeat = priority % 5 + 1;
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < repeat; i++) {
                builder.append(letter);
            }
            priorityPrefix = builder.toString();
        }

        return resize(UUID + priorityPrefix + player.getName());
    }

    @Override
    public void createTeam(Player player, String name, WoolMatch match) {
        removePlayer(player);

        Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getTeam(name);

        if (team == null) {
            team = scoreboard.registerNewTeam(name);
            team.setAllowFriendlyFire(true);
        }

        updateTeam(player, team, match);
        team.addEntry(player.getName());
        teams.put(player.getName(), team);
    }

    @Override
    public void updateTeam(Player player, Team team, WoolMatch match) {
        team.setPrefix(resize(getPrefix(player, match)));
        team.setSuffix(resize(getSuffix(player, match)));
    }

    @Override
    public void removePlayer(Player player) {
        Team team = teams.get(player.getName());
        if (team != null) {
            team.removeEntry(player.getName());
            teams.remove(player.getName());
            if (team.getEntries().isEmpty()) team.unregister();
        }
    }

    @Override
    public String getPrefix(Player player, WoolMatch match) {
        if (match == null) return vaultAPIHook != null ? vaultAPIHook.getPrefix(player) : "";
        if (match.getPlayerHolder().isSpectator(player)) return ChatColor.GRAY + "[SPECTATOR] ";

        WoolMatchStats stats = match.getPlayerHolder().getMatchStats(player);
        if (stats.getTeam() == null) return "";

        return stats.getTeam().getTeamColor().getCC() + ChatColor.BOLD.toString()
                + stats.getTeam().getTeamColor().getTag() + stats.getTeam().getTeamColor().getCC() + " ";
    }

    @Override
    public String getSuffix(Player player, WoolMatch match) {
        return "";
    }

    @Override
    public void updateDisplayName(Player player, WoolMatch match) {
        player.setDisplayName(getPrefix(player, match) + player.getName() + getSuffix(player, match));
    }

    @Override
    public void updateTab(Player player, WoolMatch match) {
        player.setPlayerListName(getPrefix(player, match) + player.getName() + getSuffix(player, match));
    }

    @Override
    public void updatePrefixAndSuffix(Player player, WoolMatch match, String teamName) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Scoreboard scoreboard = onlinePlayer.getScoreboard();

            Team scoreboardTeam = scoreboard.getTeam(teamName);
            if (scoreboardTeam == null) {
                scoreboardTeam = scoreboard.registerNewTeam(teamName);
            }

            scoreboardTeam.setPrefix(resize(getPrefix(player, match)));
            scoreboardTeam.setSuffix(resize(getSuffix(player, match)));

            scoreboardTeam.addEntry(player.getName());
        }
    }

    @Override
    public int getPriority(Player player, WoolMatch match) {
        if (match == null) return 0;

        if (match.getPlayerHolder().isSpectator(player)) return 1000;

        WoolMatchStats matchStats = match.getPlayerHolder().getMatchStats(player);
        if (matchStats.getTeam() == null) return 0;

        return matchStats.getTeam().getTeamColor().getPriority();
    }

    @Override
    public boolean isTracked(Player player) {
        return teams.containsKey(player.getName());
    }

    @Override
    public void trackPlayer(Player player, WoolMatch match) {
        update(player, match);
    }

    @Override
    public void stopTrackPlayer(Player player) {
        removePlayer(player);

        player.setDisplayName(player.getName());
        player.setPlayerListName(player.getName());
    }

    @Override
    public String resize(String string) {
        return string.length() > 16 ? string.substring(0, 16) : string;
    }


}

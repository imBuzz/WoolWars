package me.buzz.woolwars.game.player;

import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.player.stats.WoolMatchStats;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;

public class TabHandler {

    private static final String UUID = generateUUID();
    private final Map<String, Team> teams = new HashMap<>();

    private static String generateUUID() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            builder.append((char) (65 + Math.random() * 26));
        }
        return builder.toString();
    }

    public void update(Player player, WoolMatch match) {
        Team team = teams.get(player.getName());
        String newTeamName = resize(UUID + player.getName());

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
    }

    private void createTeam(Player player, String name, WoolMatch match) {
        removePlayer(player);

        Team team = player.getScoreboard().getTeam(name);

        if (team == null) {
            team = player.getScoreboard().registerNewTeam(name);
            team.setAllowFriendlyFire(true);
        }

        updateTeam(player, team, match);
        team.addEntry(player.getName());
        teams.put(player.getName(), team);
    }

    private void updateTeam(Player player, Team team, WoolMatch match) {
        team.setPrefix(resize(getPrefix(player, match)));
        team.setSuffix(resize(getSuffix(player, match)));
    }

    private void removePlayer(Player player) {
        Team team = teams.get(player.getName());
        if (team != null) {
            team.removeEntry(player.getName());
            teams.remove(player.getName());
            if (team.getEntries().isEmpty()) team.unregister();
        }
    }

    private String getPrefix(Player player, WoolMatch match) {
        if (match.getPlayerHolder().isSpectator(player)) return ChatColor.GRAY + "[SPECTATOR] ";

        WoolMatchStats stats = match.getPlayerHolder().getMatchStats(player);
        if (stats.getTeam() == null) return "";

        return stats.getTeam().getTeamColor().getCC() + ChatColor.BOLD.toString()
                + stats.getTeam().getTeamColor().getTag() + stats.getTeam().getTeamColor().getCC() + " ";
    }

    private String getSuffix(Player player, WoolMatch match) {
        return "";
    }

    private void updateDisplayName(Player player, WoolMatch match) {
        player.setDisplayName(getPrefix(player, match) + player.getName());
    }

    private void updateTab(Player player, WoolMatch match) {
        player.setPlayerListName(getPrefix(player, match) + player.getName() + getSuffix(player, match));
    }

    public boolean isTracked(Player player) {
        return teams.containsKey(player.getName());
    }

    public void trackPlayer(Player player, WoolMatch match) {
        update(player, match);
    }

    public void stopTrackPlayer(Player player) {
        removePlayer(player);

        player.setDisplayName(player.getName());
        player.setPlayerListName(player.getName());
    }

    public String resize(String string) {
        return string.length() > 16 ? string.substring(0, 16) : string;
    }

}

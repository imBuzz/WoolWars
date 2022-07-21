package me.buzz.woolwars.game.player.tablist;

import me.buzz.woolwars.game.game.match.WoolMatch;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public interface ITabHandler {

    void update(Player player, WoolMatch match);

    String getTeamName(Player player, WoolMatch match);

    void createTeam(Player player, String name, WoolMatch match);

    void updateTeam(Player player, Team team, WoolMatch match);

    void removePlayer(Player player);

    String getPrefix(Player player, WoolMatch match);

    String getSuffix(Player player, WoolMatch match);

    void updateDisplayName(Player player, WoolMatch match);

    void updateTab(Player player, WoolMatch match);

    void updatePrefixAndSuffix(Player player, WoolMatch match, String teamName);

    int getPriority(Player player, WoolMatch match);

    boolean isTracked(Player player);

    void trackPlayer(Player player, WoolMatch match);

    void stopTrackPlayer(Player player);

    String resize(String string);


}

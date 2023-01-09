package me.buzz.woolwars.game.player.tablist.impl;

import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.player.tablist.ITabHandler;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class EmptyTabHandler implements ITabHandler {

    @Override
    public void update(Player player, WoolMatch match) {

    }

    @Override
    public String getTeamName(Player player, WoolMatch match) {
        return null;
    }

    @Override
    public void createTeam(Player player, String name, WoolMatch match) {

    }

    @Override
    public void updateTeam(Player player, Team team, WoolMatch match) {

    }

    @Override
    public void removePlayer(Player player) {

    }

    @Override
    public String getPrefix(Player player, WoolMatch match) {
        return null;
    }

    @Override
    public String getSuffix(Player player, WoolMatch match) {
        return null;
    }

    @Override
    public void updateDisplayName(Player player, WoolMatch match) {

    }

    @Override
    public void updateTab(Player player, WoolMatch match) {

    }

    @Override
    public void updatePrefixAndSuffix(Player player, WoolMatch match, String teamName) {

    }

    @Override
    public int getPriority(Player player, WoolMatch match) {
        return 0;
    }

    @Override
    public boolean isTracked(Player player) {
        return false;
    }

    @Override
    public void trackPlayer(Player player, WoolMatch match) {

    }

    @Override
    public void stopTrackPlayer(Player player) {

    }

    @Override
    public String resize(String string) {
        return null;
    }
}

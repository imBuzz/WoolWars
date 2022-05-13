package me.buzz.woolwars.game.game.match.round;

import me.buzz.woolwars.api.game.arena.region.ArenaRegionType;
import me.buzz.woolwars.api.game.match.state.MatchState;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.player.PlayerHolder;
import me.buzz.woolwars.game.game.match.player.classes.PlayableClass;
import me.buzz.woolwars.game.game.match.player.classes.classes.BerserkPlayableClass;
import me.buzz.woolwars.game.game.match.player.stats.MatchStats;
import me.buzz.woolwars.game.game.match.player.team.impl.WoolTeam;
import me.buzz.woolwars.game.game.match.task.CooldownTask;
import me.buzz.woolwars.game.manager.AbstractHolder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class RoundHolder extends AbstractHolder {

    private final PlayerHolder playerHolder = match.getPlayerHolder();

    public RoundHolder(WoolMatch match) {
        super(match);
    }

    public void startNewRound() {
        if (match.getMatchState() == MatchState.ENDING) return;

        for (Block block : match.getPlayableArena().getRegion(ArenaRegionType.RED_WALL).getBlocks()) {
            block.setType(Material.GLASS);
        }
        for (Block block : match.getPlayableArena().getRegion(ArenaRegionType.BLUE_WALL).getBlocks()) {
            block.setType(Material.GLASS);
        }

        for (WoolTeam team : match.getTeams()) {
            for (Player onlinePlayer : team.getOnlinePlayers()) {
                if (playerHolder.isSpectator(onlinePlayer)) playerHolder.removeSpectator(onlinePlayer);

                MatchStats matchStats = playerHolder.getMatchStats(onlinePlayer);
                PlayableClass selectedClass = matchStats.getPlayableClass();
                if (selectedClass == null) {
                    selectedClass = new BerserkPlayableClass(onlinePlayer, matchStats.getTeam().getTeamColor());
                    matchStats.setPlayableClass(selectedClass);
                }
                selectedClass.equip();

                onlinePlayer.teleport(team.getSpawnLocation());
            }
        }

        new CooldownTask(match, () -> {
            if (match.getMatchState() != MatchState.PLAYING) match.setMatchState(MatchState.PLAYING);
            removeWalls();
            for (Player onlinePlayer : playerHolder.getOnlinePlayers()) {
                onlinePlayer.sendMessage("Started");
            }
        }, 5).runTaskTimer(WoolWars.get(), 0L, 20L);
    }

    public void endRound(WoolTeam woolTeam) {
        woolTeam.increasePoints(1);

        for (Player onlinePlayer : playerHolder.getOnlinePlayers()) {
            playerHolder.setSpectator(onlinePlayer);
        }

        new CooldownTask(match, this::startNewRound, 5).runTaskTimer(WoolWars.get(), 0L, 20L);
    }

    public void removeWalls() {
        for (Block block : match.getPlayableArena().getRegion(ArenaRegionType.RED_WALL).getBlocks())
            block.setType(Material.AIR);
        for (Block block : match.getPlayableArena().getRegion(ArenaRegionType.BLUE_WALL).getBlocks())
            block.setType(Material.AIR);
    }


}

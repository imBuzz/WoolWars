package me.buzz.woolwars.game.game.match.round;

import lombok.Getter;
import me.buzz.woolwars.api.game.arena.region.ArenaRegionType;
import me.buzz.woolwars.api.game.match.state.MatchState;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.LanguageFile;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.player.PlayerHolder;
import me.buzz.woolwars.game.game.match.player.classes.PlayableClass;
import me.buzz.woolwars.game.game.match.player.classes.classes.BerserkPlayableClass;
import me.buzz.woolwars.game.game.match.player.stats.MatchStats;
import me.buzz.woolwars.game.game.match.player.team.color.TeamColor;
import me.buzz.woolwars.game.game.match.player.team.impl.WoolTeam;
import me.buzz.woolwars.game.game.match.task.CooldownTask;
import me.buzz.woolwars.game.manager.AbstractHolder;
import me.buzz.woolwars.game.utils.StringsUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class RoundHolder extends AbstractHolder {

    private final PlayerHolder playerHolder = match.getPlayerHolder();
    @Getter
    private int roundNumber = 0;

    public RoundHolder(WoolMatch match) {
        super(match);
    }

    public void startNewRound() {
        match.setMatchState(MatchState.PRE_ROUND);
        roundNumber++;

        for (Block block : match.getPlayableArena().getRegion(ArenaRegionType.RED_WALL).getBlocks()) {
            block.setType(Material.GLASS);
        }
        for (Block block : match.getPlayableArena().getRegion(ArenaRegionType.BLUE_WALL).getBlocks()) {
            block.setType(Material.GLASS);
        }

        for (WoolTeam team : match.getTeams().values()) {
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

                onlinePlayer.sendTitle(
                        StringsUtils.colorize(match.getLanguage().getProperty(LanguageFile.PRE_ROUND_TITLE)),
                        StringsUtils.colorize(match.getLanguage().getProperty(LanguageFile.PRE_ROUND_SUBTITLE)));
            }
        }

        new CooldownTask(match, () -> {
            match.setMatchState(MatchState.ROUND);
            removeWalls();
            for (Player onlinePlayer : playerHolder.getOnlinePlayers()) {
                onlinePlayer.sendTitle(
                        StringsUtils.colorize(match.getLanguage().getProperty(LanguageFile.ROUND_START_TITLE)),
                        StringsUtils.colorize(match.getLanguage().getProperty(LanguageFile.ROUND_START_SUBTITLE)
                                .replace("%number%", String.valueOf(roundNumber))));
            }
        }, 5).runTaskTimer(WoolWars.get(), 0L, 20L);
    }

    public void endRound(WoolTeam woolTeam) {
        woolTeam.increasePoints(1);
        match.setMatchState(MatchState.ROUND_OVER);

        for (Player onlinePlayer : playerHolder.getOnlinePlayers()) {
            playerHolder.setSpectator(onlinePlayer);
            onlinePlayer.sendTitle(
                    StringsUtils.colorize(match.getLanguage().getProperty(LanguageFile.ROUND_OVER_TITLE)
                            .replace("%blue_team_points%", String.valueOf(match.getTeams().get(TeamColor.BLUE).getPoints()))
                            .replace("%red_team_points%", String.valueOf(match.getTeams().get(TeamColor.RED).getPoints()))),
                    StringsUtils.colorize(match.getLanguage().getProperty(LanguageFile.ROUND_OVER_SUBTITLE)
                    ).replace("%blue_team_points%", String.valueOf(match.getTeams().get(TeamColor.BLUE).getPoints())
                            .replace("%red_team_points%", String.valueOf(match.getTeams().get(TeamColor.RED).getPoints()))));
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

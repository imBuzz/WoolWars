package me.buzz.woolwars.game.game.match.round;

import lombok.Getter;
import lombok.Setter;
import me.buzz.woolwars.api.game.arena.region.ArenaRegionType;
import me.buzz.woolwars.api.game.match.state.MatchState;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.ConfigFile;
import me.buzz.woolwars.game.configuration.files.LanguageFile;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.player.PlayerHolder;
import me.buzz.woolwars.game.game.match.player.classes.PlayableClass;
import me.buzz.woolwars.game.game.match.player.classes.classes.BerserkPlayableClass;
import me.buzz.woolwars.game.game.match.player.stats.MatchStats;
import me.buzz.woolwars.game.game.match.player.team.color.TeamColor;
import me.buzz.woolwars.game.game.match.player.team.impl.WoolTeam;
import me.buzz.woolwars.game.game.match.task.CooldownTask;
import me.buzz.woolwars.game.game.match.task.tasks.StartRoundTask;
import me.buzz.woolwars.game.game.match.task.tasks.WaitForNewRoundTask;
import me.buzz.woolwars.game.manager.AbstractHolder;
import me.buzz.woolwars.game.utils.StringsUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RoundHolder extends AbstractHolder {

    private final PlayerHolder playerHolder = match.getPlayerHolder();
    @Getter
    private final Map<String, CooldownTask> tasks = new HashMap<>();

    @Getter
    @Setter
    private boolean canBreakCenter = false;
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

        tasks.put("startRound", new StartRoundTask(match,
                TimeUnit.SECONDS.toMillis(WoolWars.get().getSettings().getProperty(ConfigFile.PRE_ROUND_TIMER))).start(20));
    }

    public void endRound(WoolTeam woolTeam) {
        woolTeam.increasePoints(1);
        match.setMatchState(MatchState.ROUND_OVER);

        for (Player onlinePlayer : playerHolder.getOnlinePlayers()) {
            playerHolder.setSpectator(onlinePlayer);
            onlinePlayer.sendTitle(
                    StringsUtils.colorize(match.getLanguage().getProperty(LanguageFile.ROUND_OVER_TITLE)
                            .replace("{blue_team_points}", String.valueOf(match.getTeams().get(TeamColor.BLUE).getPoints()))
                            .replace("{red_team_points}", String.valueOf(match.getTeams().get(TeamColor.RED).getPoints()))),
                    StringsUtils.colorize(match.getLanguage().getProperty(LanguageFile.ROUND_OVER_SUBTITLE)
                    ).replace("{blue_team_points}", String.valueOf(match.getTeams().get(TeamColor.BLUE).getPoints())
                            .replace("{red_team_points}", String.valueOf(match.getTeams().get(TeamColor.RED).getPoints()))));
        }

        tasks.put("waitForNewRound", new WaitForNewRoundTask(match,
                TimeUnit.SECONDS.toMillis(WoolWars.get().getSettings().getProperty(ConfigFile.WAIT_FOR_NEW_ROUND_TIMER))).start(20));
    }

    public void removeWalls() {
        for (Block block : match.getPlayableArena().getRegion(ArenaRegionType.RED_WALL).getBlocks())
            block.setType(Material.AIR);
        for (Block block : match.getPlayableArena().getRegion(ArenaRegionType.BLUE_WALL).getBlocks())
            block.setType(Material.AIR);
    }


}

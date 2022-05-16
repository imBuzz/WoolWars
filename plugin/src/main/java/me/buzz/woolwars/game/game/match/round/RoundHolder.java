package me.buzz.woolwars.game.game.match.round;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.Setter;
import me.buzz.woolwars.api.game.arena.region.ArenaRegionType;
import me.buzz.woolwars.api.game.match.state.MatchState;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.ConfigFile;
import me.buzz.woolwars.game.configuration.files.LanguageFile;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.player.PlayerHolder;
import me.buzz.woolwars.game.game.match.player.stats.MatchStats;
import me.buzz.woolwars.game.game.match.player.team.color.TeamColor;
import me.buzz.woolwars.game.game.match.player.team.impl.WoolTeam;
import me.buzz.woolwars.game.game.match.task.CooldownTask;
import me.buzz.woolwars.game.game.match.task.tasks.StartRoundTask;
import me.buzz.woolwars.game.game.match.task.tasks.WaitForNewRoundTask;
import me.buzz.woolwars.game.manager.AbstractHolder;
import me.buzz.woolwars.game.utils.random.RandomSelector;
import me.buzz.woolwars.game.utils.structures.Title;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RoundHolder extends AbstractHolder {

    private final static RandomSelector<Material> centerMats = RandomSelector.uniform(ImmutableList.of(Material.SNOW_BLOCK, Material.WOOL, Material.QUARTZ_BLOCK));

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

        for (Block block : match.getPlayableArena().getRegion(ArenaRegionType.RED_WALL).getBlocks())
            block.setType(Material.GLASS);
        for (Block block : match.getPlayableArena().getRegion(ArenaRegionType.BLUE_WALL).getBlocks())
            block.setType(Material.GLASS);
        for (Block block : match.getPlayableArena().getRegion(ArenaRegionType.CENTER).getBlocks())
            block.setType(centerMats.pick());

        for (WoolTeam team : match.getTeams().values()) {
            for (Player onlinePlayer : team.getOnlinePlayers()) {
                if (playerHolder.isSpectator(onlinePlayer)) playerHolder.removeSpectator(onlinePlayer);

                MatchStats matchStats = playerHolder.getMatchStats(onlinePlayer);

                matchStats.pickClass(onlinePlayer, matchStats.getTeam().getTeamColor());
                matchStats.getPlayableClass().equip(playerHolder.getWoolPlayer(onlinePlayer),
                        playerHolder.getMatchStats(onlinePlayer));

                team.getTeamNPC().show(onlinePlayer);
                onlinePlayer.teleport(team.getSpawnLocation());

                Title title = WoolWars.get().getLanguage().getProperty(LanguageFile.PRE_ROUND_TITLE);
                onlinePlayer.sendTitle(title.getTitle(), title.getSubTitle());
            }
        }

        tasks.put(StartRoundTask.ID, new StartRoundTask(match,
                TimeUnit.SECONDS.toMillis(WoolWars.get().getSettings().getProperty(ConfigFile.PRE_ROUND_TIMER))).start());
    }

    public void endRound(WoolTeam woolTeam) {
        CooldownTask task = getTasks().remove(WaitForNewRoundTask.ID);
        if (task != null) task.stop();

        woolTeam.increasePoints(1);
        if (woolTeam.getPoints() == match.getPointsToWin()) {
            match.setMatchState(MatchState.ENDING);
            match.end(woolTeam);
            return;
        }

        match.setMatchState(MatchState.ROUND_OVER);
        for (Player onlinePlayer : playerHolder.getOnlinePlayers()) {
            playerHolder.setSpectator(onlinePlayer);

            Title title = WoolWars.get().getLanguage().getProperty(LanguageFile.ROUND_OVER_TITLE);
            onlinePlayer.sendTitle(title.getTitle()
                            .replace("{blue_team_points}", String.valueOf(match.getTeams().get(TeamColor.BLUE).getPoints()))
                            .replace("{red_team_points}", String.valueOf(match.getTeams().get(TeamColor.RED).getPoints())),
                    title.getSubTitle().replace("{blue_team_points}", String.valueOf(match.getTeams().get(TeamColor.BLUE).getPoints())
                            .replace("{red_team_points}", String.valueOf(match.getTeams().get(TeamColor.RED).getPoints()))));
        }

        tasks.put(WaitForNewRoundTask.ID, new WaitForNewRoundTask(match,
                TimeUnit.SECONDS.toMillis(WoolWars.get().getSettings().getProperty(ConfigFile.WAIT_FOR_NEW_ROUND_TIMER))).start());
    }

    public void reset() {
        for (CooldownTask value : tasks.values()) value.stop();
        tasks.clear();
        roundNumber = 0;
        canBreakCenter = false;
    }

    public void removeWalls() {
        for (Block block : match.getPlayableArena().getRegion(ArenaRegionType.RED_WALL).getBlocks())
            block.setType(Material.AIR);
        for (Block block : match.getPlayableArena().getRegion(ArenaRegionType.BLUE_WALL).getBlocks())
            block.setType(Material.AIR);

        for (WoolTeam value : match.getTeams().values()) {
            for (Player onlinePlayer : value.getOnlinePlayers()) {
                value.getTeamNPC().hide(onlinePlayer);
            }
        }

    }


}

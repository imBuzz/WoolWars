package me.buzz.woolwars.game.game.match.round;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.hakan.core.HCore;
import lombok.Getter;
import me.buzz.woolwars.api.game.arena.region.ArenaRegionType;
import me.buzz.woolwars.api.game.arena.region.Region;
import me.buzz.woolwars.api.game.match.player.player.classes.PlayableClassType;
import me.buzz.woolwars.api.game.match.player.team.TeamColor;
import me.buzz.woolwars.api.game.match.round.ApiRoundHolder;
import me.buzz.woolwars.api.game.match.state.MatchState;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.ConfigFile;
import me.buzz.woolwars.game.configuration.files.lang.LanguageFile;
import me.buzz.woolwars.game.game.arena.location.SerializedLocation;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.entities.powerup.EntityPowerup;
import me.buzz.woolwars.game.game.match.entities.powerup.PowerUPType;
import me.buzz.woolwars.game.game.match.player.PlayerMatchHolder;
import me.buzz.woolwars.game.game.match.player.stats.WoolMatchStats;
import me.buzz.woolwars.game.game.match.player.team.impl.WoolTeam;
import me.buzz.woolwars.game.game.match.task.CooldownTask;
import me.buzz.woolwars.game.game.match.task.tasks.StartRoundTask;
import me.buzz.woolwars.game.game.match.task.tasks.WaitForNewRoundTask;
import me.buzz.woolwars.game.manager.AbstractMatchHolder;
import me.buzz.woolwars.game.utils.TeamUtils;
import me.buzz.woolwars.game.utils.random.RandomSelector;
import me.buzz.woolwars.game.utils.structures.Title;
import me.buzz.woolwars.game.utils.workload.WorkloadHandler;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RoundMatchHolder extends AbstractMatchHolder implements ApiRoundHolder {

    private final static RandomSelector<XMaterial> centerMats = RandomSelector.uniform(ImmutableList.of(XMaterial.SNOW_BLOCK, XMaterial.WHITE_WOOL, XMaterial.QUARTZ_BLOCK));
    private final static RandomSelector<PowerUPType> powerUPS = RandomSelector.uniform(Lists.newArrayList(PowerUPType.values()));

    @Getter
    private final Map<String, CooldownTask> tasks = new HashMap<>();
    @Getter
    private final List<EntityPowerup> entities = new ArrayList<>();

    private final PlayerMatchHolder playerHolder = match.getPlayerHolder();

    public boolean canBreakCenter = false;
    @Getter
    private int roundNumber = 0;

    public RoundMatchHolder(WoolMatch match) {
        super(match);
    }

    public void startNewRound() {
        match.setMatchState(MatchState.PRE_ROUND);
        roundNumber++;

        WorkloadHandler.addLoad(this::makeWalls);

        spawnGenerators();

        for (WoolTeam team : match.getTeams().values()) {
            for (Player onlinePlayer : team.getOnlinePlayers()) {
                if (playerHolder.isSpectator(onlinePlayer)) playerHolder.removeSpectator(onlinePlayer);

                WoolMatchStats matchStats = playerHolder.getMatchStats(onlinePlayer);

                matchStats.pickClass(playerHolder.getWoolPlayer(onlinePlayer), matchStats,
                        matchStats.getPlayableClass() == null ? PlayableClassType.TANK : matchStats.getClassType());
                team.getTeamNPC().addViewer(onlinePlayer);

                onlinePlayer.teleport(team.getSpawnLocation());
                WoolWars.get().getSettings().getProperty(ConfigFile.SOUNDS_TELEPORT).play(onlinePlayer, 1, 1);

                WoolWars.get().getTabHandler().update(onlinePlayer, match);

                Title title = WoolWars.get().getLanguage().getProperty(LanguageFile.PRE_ROUND_TITLE);
                HCore.sendTitle(onlinePlayer, title.getTitle(), title.getSubTitle());
                WoolWars.get().getSettings().getProperty(ConfigFile.SOUNDS_ROUND_START).play(onlinePlayer, 1, 1);
            }
        }

        tasks.put(StartRoundTask.ID, new StartRoundTask(match,
                TimeUnit.SECONDS.toMillis(WoolWars.get().getSettings().getProperty(ConfigFile.PRE_ROUND_TIMER))).start());
    }

    public void endRound(WoolTeam woolTeam) {
        tasks.values().forEach(CooldownTask::stop);
        tasks.clear();

        despawnGenerators();

        if (woolTeam == null) {
            Region centerRegion = match.getArena().getRegion(ArenaRegionType.CENTER);
            Map<DyeColor, Integer> blockPlacedPerTeamColor = new HashMap<>();
            for (Block block : centerRegion.getBlocks()) {
                if (!block.getType().toString().contains("WOOL")) continue;

                DyeColor dyeColor = DyeColor.getByWoolData(block.getData());
                blockPlacedPerTeamColor.putIfAbsent(dyeColor, 0);
                blockPlacedPerTeamColor.put(dyeColor, blockPlacedPerTeamColor.get(dyeColor) + 1);
            }

            List<DyeColor> colors = TeamUtils.getTopTeamPlacedByWoolColor(blockPlacedPerTeamColor);
            if (!blockPlacedPerTeamColor.isEmpty() || !TeamUtils.testBlocksColorTruth(colors, blockPlacedPerTeamColor)) {
                woolTeam = match.getTeams().get(TeamColor.fromDyeColor(colors.get(0)));
            }
        }

        if (woolTeam != null) {
            woolTeam.increasePoints(1);
            if (woolTeam.getPoints() == match.getPointsToWin()) {
                match.end(woolTeam);
                return;
            }
        }

        match.setMatchState(MatchState.ROUND_OVER);
        for (Player onlinePlayer : playerHolder.getOnlinePlayers()) {
            playerHolder.setSpectator(onlinePlayer);

            Title title = WoolWars.get().getLanguage().getProperty(LanguageFile.ROUND_OVER_TITLE);
            HCore.sendTitle(onlinePlayer, title.getTitle()
                            .replace("{blue_team_points}", String.valueOf(match.getTeams().get(TeamColor.BLUE).getPoints()))
                            .replace("{red_team_points}", String.valueOf(match.getTeams().get(TeamColor.RED).getPoints())),
                    title.getSubTitle().replace("{blue_team_points}", String.valueOf(match.getTeams().get(TeamColor.BLUE).getPoints())
                            .replace("{red_team_points}", String.valueOf(match.getTeams().get(TeamColor.RED).getPoints()))));

            WoolMatchStats stats = playerHolder.getMatchStats(onlinePlayer);
            if (stats != null) {
                if (stats.getTeam() == woolTeam) {
                    WoolWars.get().getSettings().getProperty(ConfigFile.SOUNDS_ROUND_WON).play(onlinePlayer, 1, 1);
                } else {
                    WoolWars.get().getSettings().getProperty(ConfigFile.SOUNDS_ROUND_LOST).play(onlinePlayer, 1, 1);
                }
            }

        }

        tasks.put(WaitForNewRoundTask.ID, new WaitForNewRoundTask(match,
                TimeUnit.SECONDS.toMillis(WoolWars.get().getSettings().getProperty(ConfigFile.WAIT_FOR_NEW_ROUND_TIMER))).start());
    }

    public void reset() {
        for (CooldownTask value : tasks.values()) value.stop();
        tasks.clear();

        despawnGenerators();

        roundNumber = 0;
        canBreakCenter = false;

        makeWalls();
    }

    public void makeWalls() {
        for (Block block : match.getPlayableArena().getRegion(ArenaRegionType.RED_WALL).getBlocks())
            block.setType(XMaterial.GLASS.parseMaterial());
        for (Block block : match.getPlayableArena().getRegion(ArenaRegionType.BLUE_WALL).getBlocks())
            block.setType(XMaterial.GLASS.parseMaterial());
        for (Block block : match.getPlayableArena().getRegion(ArenaRegionType.CENTER).getBlocks())
            block.setType(centerMats.pick().parseMaterial());
    }

    public void removeWalls() {
        WorkloadHandler.addLoad(() -> {
            for (Block block : match.getPlayableArena().getRegion(ArenaRegionType.RED_WALL).getBlocks())
                block.setType(Material.AIR);
            for (Block block : match.getPlayableArena().getRegion(ArenaRegionType.BLUE_WALL).getBlocks())
                block.setType(Material.AIR);
        });
        for (WoolTeam value : match.getTeams().values()) {
            for (Player onlinePlayer : value.getOnlinePlayers()) {
                onlinePlayer.closeInventory();
                //if (value.getTeamNPC().getRenderer().canSee(onlinePlayer.getUniqueId()))
                value.getTeamNPC().removeViewer(onlinePlayer);
            }
        }
    }

    public void spawnGenerators() {
        List<PowerUPType> alreadyPicked = new ArrayList<>();
        for (SerializedLocation powerup : match.getPlayableArena().getPowerups()) {
            PowerUPType picked;

            do {
                picked = powerUPS.pick();
            }
            while (alreadyPicked.contains(picked));

            alreadyPicked.add(picked);

            EntityPowerup powerUP = new EntityPowerup(match, picked, powerup.toBukkitLocation(match.getArena().getWorld()));
            powerUP.spawn();
            powerUP.setName(WoolWars.get().getLanguage().getProperty(picked.getProperty()).getHoloName());

            entities.add(powerUP);
        }
    }

    public void despawnGenerators() {
        entities.forEach(EntityPowerup::die);
        entities.clear();
    }

    @Override
    public boolean canBreakCenter() {
        return canBreakCenter;
    }

    @Override
    public int getPointsByTeamColor(TeamColor teamColor) {
        return match.getTeams().containsKey(teamColor) ? match.getTeams().get(teamColor).getPoints() : 0;
    }
}

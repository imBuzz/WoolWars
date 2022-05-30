package me.buzz.woolwars.game.game.arena.settings.preset.impl;

import me.buzz.woolwars.api.game.match.player.team.TeamColor;
import me.buzz.woolwars.api.game.match.state.MatchState;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.lang.LanguageFile;
import me.buzz.woolwars.game.game.arena.settings.preset.ApplicablePreset;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.task.tasks.TimeElapsedTask;
import me.buzz.woolwars.game.game.match.task.tasks.WaitForNewRoundTask;
import me.buzz.woolwars.game.hook.ExternalPluginHook;
import me.buzz.woolwars.game.hook.ImplementedHookType;
import me.buzz.woolwars.game.utils.StringsUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreboardPreset implements ApplicablePreset<List<String>, WoolMatch, Player, Void> {

    private final Map<MatchState, List<String>> lines = new HashMap<>();

    public ScoreboardPreset(FileConfiguration data) {
        for (String matchState : data.getConfigurationSection("options.scoreboard").getKeys(false)) {
            try {
                MatchState state = MatchState.valueOf(matchState.toUpperCase());
                lines.put(state, data.getStringList("options.scoreboard." + matchState));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<String> apply(WoolMatch match, Player player, Void v) {
        List<String> strings, tempLines = new ArrayList<>();
        ExternalPluginHook<String, Player> placeholderHook = WoolWars.get().getHook(ImplementedHookType.PLACEHOLDER_API);
        if (match == null) {
            strings = WoolWars.get().getLanguage().getProperty(LanguageFile.SCOREBOARD_MATCH_LOBBY);
            for (String line : strings) {
                tempLines.add(placeholderHook != null ? placeholderHook.apply(line, player) : line);
            }
            return tempLines;
        }

        strings = lines.getOrDefault(match.getMatchState(), new ArrayList<>());
        switch (match.getMatchState()) {
            case WAITING: {
                for (String line : strings) {
                    line = line.replace("{map_name}", match.getArena().getName())
                            .replace("{current_players}", String.valueOf(match.getPlayerHolder().getPlayersCount()))
                            .replace("{max_players}", String.valueOf(match.getMaxPlayers()));
                    tempLines.add(placeholderHook != null ? placeholderHook.apply(line, player) : line);
                }
                break;
            }
            case STARTING: {
                for (String line : strings) {
                    line = line.replace("{map_name}", match.getArena().getName())
                            .replace("{current_players}", String.valueOf(match.getPlayerHolder().getPlayersCount()))
                            .replace("{max_players}", String.valueOf(match.getMaxPlayers()))
                            .replace("{remaning_seconds}", String.valueOf(match.getRoundHolder()
                                    .getTasks().get("startTask").getRemainingSeconds()));

                    tempLines.add(placeholderHook != null ? placeholderHook.apply(line, player) : line);
                }
                break;
            }
            case PRE_ROUND: {
                for (String line : strings) {
                    line = line.replace("{round}", String.valueOf(match.getRoundHolder().getRoundNumber()))
                            .replace("{round_type}", WoolWars.get().getLanguage().getProperty(LanguageFile.ROUND_PRE_ROUND))
                            .replace("{map_name}", match.getArena().getName())

                            .replace("{red_team_points}", String.valueOf(match.getTeams().get(TeamColor.RED).getPoints()))
                            .replace("{blue_team_points}", String.valueOf(match.getTeams().get(TeamColor.BLUE).getPoints()))

                            .replace("{red_team_progress}",
                                    StringsUtils.getProgressBar(match.getTeams().get(TeamColor.RED).getPoints(), 3, 3,
                                            WoolWars.get().getLanguage().getProperty(LanguageFile.PROGRESS_SYMBOL).toCharArray()[0], ChatColor.RED, ChatColor.GRAY))
                            .replace("{blue_team_progress}",
                                    StringsUtils.getProgressBar(match.getTeams().get(TeamColor.BLUE).getPoints(), 3, 3,
                                            WoolWars.get().getLanguage().getProperty(LanguageFile.PROGRESS_SYMBOL).toCharArray()[0], ChatColor.BLUE, ChatColor.GRAY))

                            .replace("{time_left}", match.getRoundHolder().getTasks().get("startRound").formatSeconds())

                            .replace("{red_team_players}", String.valueOf(match.getTeams().get(TeamColor.RED).getPlayers().size()))
                            .replace("{blue_team_players}", String.valueOf(match.getTeams().get(TeamColor.BLUE).getPlayers().size()));

                    tempLines.add(placeholderHook != null ? placeholderHook.apply(line, player) : line);
                }
                break;
            }
            case ROUND: {
                for (String line : strings) {
                    line = line.replace("{round}", String.valueOf(match.getRoundHolder().getRoundNumber()))
                            .replace("{round_type}", getMatchName(match))
                            .replace("{map_name}", match.getArena().getName())

                            .replace("{red_team_progress}",
                                    StringsUtils.getProgressBar(match.getTeams().get(TeamColor.RED).getPoints(), 3, 3,
                                            WoolWars.get().getLanguage().getProperty(LanguageFile.PROGRESS_SYMBOL).toCharArray()[0], ChatColor.RED, ChatColor.GRAY))
                            .replace("{blue_team_progress}",
                                    StringsUtils.getProgressBar(match.getTeams().get(TeamColor.BLUE).getPoints(), 3, 3,
                                            WoolWars.get().getLanguage().getProperty(LanguageFile.PROGRESS_SYMBOL).toCharArray()[0], ChatColor.BLUE, ChatColor.GRAY))

                            .replace("{time_left}",
                                    match.getRoundHolder().getTasks().containsKey(TimeElapsedTask.ID) ? match.getRoundHolder().getTasks().get(TimeElapsedTask.ID).formatSeconds() : "00:00")

                            .replace("{red_team_points}", String.valueOf(match.getTeams().get(TeamColor.RED).getPoints()))
                            .replace("{blue_team_points}", String.valueOf(match.getTeams().get(TeamColor.BLUE).getPoints()))

                            .replace("{red_team_isYou}", match.getTeams().get(TeamColor.RED).getPlayers().contains(player) ? WoolWars.get().getLanguage().getProperty(LanguageFile.IS_YOU) : "")
                            .replace("{blue_team_isYou}", match.getTeams().get(TeamColor.BLUE).getPlayers().contains(player) ? WoolWars.get().getLanguage().getProperty(LanguageFile.IS_YOU) : "")

                            .replace("{red_team_players}", String.valueOf(match.getTeams().get(TeamColor.RED).getPlayers().size()))
                            .replace("{blue_team_players}", String.valueOf(match.getTeams().get(TeamColor.BLUE).getPlayers().size()));

                    tempLines.add(placeholderHook != null ? placeholderHook.apply(line, player) : line);
                }
                break;
            }
            default: {
                for (String line : strings) {
                    line = line.replace("{round}", String.valueOf(match.getRoundHolder().getRoundNumber()))
                            .replace("{round_type}", getMatchName(match))
                            .replace("{map_name}", match.getArena().getName())

                            .replace("{red_team_progress}",
                                    StringsUtils.getProgressBar(match.getTeams().get(TeamColor.RED).getPoints(), 3, 3,
                                            WoolWars.get().getLanguage().getProperty(LanguageFile.PROGRESS_SYMBOL).toCharArray()[0], ChatColor.RED, ChatColor.GRAY))
                            .replace("{blue_team_progress}",
                                    StringsUtils.getProgressBar(match.getTeams().get(TeamColor.BLUE).getPoints(), 3, 3,
                                            WoolWars.get().getLanguage().getProperty(LanguageFile.PROGRESS_SYMBOL).toCharArray()[0], ChatColor.BLUE, ChatColor.GRAY))

                            .replace("{time_left}",
                                    match.getRoundHolder().getTasks().containsKey(WaitForNewRoundTask.ID) ? match.getRoundHolder().getTasks().get(WaitForNewRoundTask.ID).formatSeconds() : "00:00")

                            .replace("{red_team_points}", String.valueOf(match.getTeams().get(TeamColor.RED).getPoints()))
                            .replace("{blue_team_points}", String.valueOf(match.getTeams().get(TeamColor.BLUE).getPoints()))

                            .replace("{red_team_isYou}", match.getTeams().get(TeamColor.RED).getPlayers().contains(player) ? WoolWars.get().getLanguage().getProperty(LanguageFile.IS_YOU) : "")
                            .replace("{blue_team_isYou}", match.getTeams().get(TeamColor.BLUE).getPlayers().contains(player) ? WoolWars.get().getLanguage().getProperty(LanguageFile.IS_YOU) : "");

                    tempLines.add(placeholderHook != null ? placeholderHook.apply(line, player) : line);
                }
                break;
            }
        }

        return tempLines;
    }

    private String getMatchName(WoolMatch match) {
        switch (match.getMatchState()) {
            case WAITING:
                return WoolWars.get().getLanguage().getProperty(LanguageFile.ROUND_WAITING);
            case PRE_ROUND:
                return WoolWars.get().getLanguage().getProperty(LanguageFile.ROUND_PRE_ROUND);
            case ROUND:
                return WoolWars.get().getLanguage().getProperty(LanguageFile.ROUND_ROUND);
            default:
                return WoolWars.get().getLanguage().getProperty(LanguageFile.ROUND_ENDED);
        }
    }

}

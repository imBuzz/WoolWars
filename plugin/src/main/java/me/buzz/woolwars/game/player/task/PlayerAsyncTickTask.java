package me.buzz.woolwars.game.player.task;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.LanguageFile;
import me.buzz.woolwars.game.game.GameManager;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.player.team.color.TeamColor;
import me.buzz.woolwars.game.utils.StringsUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerAsyncTickTask extends BukkitRunnable {

    private final GameManager gameManager = WoolWars.get().getGameManager();

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            BPlayerBoard board = Netherboard.instance().getBoard(player);
            if (board == null) return;

            WoolMatch match = gameManager.getInternalMatchByPlayer(player);
            applyScoreboardLines(getScoreboardLinesByMatchState(player, match), board);
        }
    }

    public List<String> getScoreboardLinesByMatchState(Player player, WoolMatch match) {
        List<String> tempLines, lines;
        if (match == null) tempLines = WoolWars.get().getLanguage().getProperty(LanguageFile.SCOREBOARD_MATCH_LOBBY);
        else {
            switch (match.getMatchState()) {
                case WAITING: {
                    tempLines = match.getLanguage().getProperty(LanguageFile.SCOREBOARD_MATCH_WAITING);
                    break;
                }
                case STARTING: {
                    tempLines = match.getLanguage().getProperty(LanguageFile.SCOREBOARD_MATCH_STARTING);
                    break;
                }
                case PRE_ROUND: {
                    tempLines = match.getLanguage().getProperty(LanguageFile.SCOREBOARD_MATCH_PRE_ROUND);
                    break;
                }
                default: {
                    tempLines = match.getLanguage().getProperty(LanguageFile.SCOREBOARD_MATCH_ROUND);
                    break;
                }
            }
        }

        lines = new ArrayList<>();
        for (String tempLine : tempLines) {
            if (match != null) {
                switch (match.getMatchState()) {
                    case WAITING: {
                        tempLine = tempLine
                                .replace("{map_name}", match.getArena().getName())
                                .replace("{current_players}", String.valueOf(match.getPlayerHolder().getPlayersCount()))
                                .replace("{max_players}", String.valueOf(match.getMaxPlayers()));
                        break;
                    }
                    case STARTING: {
                        tempLine = tempLine
                                .replace("{map_name}", match.getArena().getName())
                                .replace("{current_players}", String.valueOf(match.getPlayerHolder().getPlayersCount()))
                                .replace("{max_players}", String.valueOf(match.getMaxPlayers()))
                                .replace("{remaning_seconds}", String.valueOf(match.getMaxPlayers()));
                        break;
                    }
                    case PRE_ROUND: {
                        tempLine = tempLine
                                .replace("{round}", String.valueOf(match.getRoundHolder().getRoundNumber()))
                                .replace("{round_type}", match.getLanguage().getProperty(LanguageFile.ROUND_PRE_ROUND))
                                .replace("{map_name}", match.getArena().getName())

                                .replace("{red_team_points}", String.valueOf(match.getTeams().get(TeamColor.RED).getPoints()))
                                .replace("{blue_team_points}", String.valueOf(match.getTeams().get(TeamColor.BLUE).getPoints()))

                                .replace("{red_team_progress}",
                                        StringsUtils.getProgressBar(match.getTeams().get(TeamColor.RED).getPoints(), 3, 3,
                                                match.getLanguage().getProperty(LanguageFile.PROGRESS_SYMBOL).toCharArray()[0], ChatColor.RED, ChatColor.GRAY))
                                .replace("{blue_team_progress}",
                                        StringsUtils.getProgressBar(match.getTeams().get(TeamColor.BLUE).getPoints(), 3, 3,
                                                match.getLanguage().getProperty(LanguageFile.PROGRESS_SYMBOL).toCharArray()[0], ChatColor.BLUE, ChatColor.GRAY))

                                .replace("{time_left}", DurationFormatUtils.formatDuration(match.getRoundHolder().task.getTargetSeconds() * 1000L, "mm:ss"))
                                .replace("{red_team_players}", String.valueOf(match.getTeams().get(TeamColor.RED).getPlayers().size()))
                                .replace("{blue_team_players}", String.valueOf(match.getTeams().get(TeamColor.BLUE).getPlayers().size()));
                        break;
                    }
                    default: {
                        tempLine = tempLine
                                .replace("{round}", String.valueOf(match.getRoundHolder().getRoundNumber()))
                                .replace("{round_type}", getMatchName(match))
                                .replace("{map_name}", match.getArena().getName())

                                .replace("{red_team_progress}",
                                        StringsUtils.getProgressBar(match.getTeams().get(TeamColor.RED).getPoints(), 3, 3,
                                                match.getLanguage().getProperty(LanguageFile.PROGRESS_SYMBOL).toCharArray()[0], ChatColor.RED, ChatColor.GRAY))
                                .replace("{blue_team_progress}",
                                        StringsUtils.getProgressBar(match.getTeams().get(TeamColor.BLUE).getPoints(), 3, 3,
                                                match.getLanguage().getProperty(LanguageFile.PROGRESS_SYMBOL).toCharArray()[0], ChatColor.BLUE, ChatColor.GRAY))
                                .replace("{time_left}", DurationFormatUtils.formatDuration(match.getRoundHolder().task.getTargetSeconds() * 1000L, "mm:ss"))

                                .replace("{red_team_points}", String.valueOf(match.getTeams().get(TeamColor.RED).getPoints()))
                                .replace("{blue_team_points}", String.valueOf(match.getTeams().get(TeamColor.BLUE).getPoints()))

                                .replace("{red_team_isYou}", match.getTeams().get(TeamColor.RED).getPlayers().contains(player) ? match.getLanguage().getProperty(LanguageFile.IS_YOU) : "")
                                .replace("{blue_team_isYou}", match.getTeams().get(TeamColor.BLUE).getPlayers().contains(player) ? match.getLanguage().getProperty(LanguageFile.IS_YOU) : "")

                                .replace("{red_team_players}", String.valueOf(match.getTeams().get(TeamColor.RED).getPlayers().size()))
                                .replace("{blue_team_players}", String.valueOf(match.getTeams().get(TeamColor.BLUE).getPlayers().size()));
                        break;
                    }
                }
            }
            lines.add(StringsUtils.colorize(tempLine));
        }

        return lines;
    }

    private String getMatchName(WoolMatch match) {
        switch (match.getMatchState()) {
            case WAITING:
                return match.getLanguage().getProperty(LanguageFile.ROUND_WAITING);
            case PRE_ROUND:
                return match.getLanguage().getProperty(LanguageFile.ROUND_PRE_ROUND);
            case ROUND:
                return match.getLanguage().getProperty(LanguageFile.ROUND_ROUND);
            default:
                return match.getLanguage().getProperty(LanguageFile.ROUND_ENDED);
        }
    }

    private void applyScoreboardLines(List<String> lines, BPlayerBoard board) {
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            board.set(line, lines.size() - i);
        }

        Set<Integer> scores = new HashSet<>(board.getLines().keySet());
        for (int score : scores) {
            if (score <= 0 || score > lines.size()) {
                board.remove(score);
            }
        }
    }

}

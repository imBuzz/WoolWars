package me.buzz.woolwars.game.game.match.player.stats;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.buzz.woolwars.api.game.match.player.events.PlayerSelectClassEvent;
import me.buzz.woolwars.api.game.match.player.player.ApiWoolMatchStats;
import me.buzz.woolwars.api.game.match.player.player.classes.PlayableClassType;
import me.buzz.woolwars.api.game.match.player.team.TeamColor;
import me.buzz.woolwars.game.game.match.player.classes.PlayableClass;
import me.buzz.woolwars.game.game.match.player.classes.classes.ArcherPlayableClass;
import me.buzz.woolwars.game.game.match.player.classes.classes.AssaultPlayableClass;
import me.buzz.woolwars.game.game.match.player.classes.classes.EngineerPlayableClass;
import me.buzz.woolwars.game.game.match.player.classes.classes.GolemPlayableClass;
import me.buzz.woolwars.game.game.match.player.classes.classes.SwordmanPlayableClass;
import me.buzz.woolwars.game.game.match.player.classes.classes.TankPlayableClass;
import me.buzz.woolwars.game.game.match.player.team.impl.WoolTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class WoolMatchStats implements ApiWoolMatchStats {
    private final UUID uuid;

    @Setter
    private WoolTeam team;
    @Setter
    private PlayableClass playableClass;

    public int matchWoolPlaced, matchBlocksBroken, matchPowerUpsGotten;
    public int matchKills, matchDeaths;

    public void pickClass(Player player, TeamColor teamColor, PlayableClassType type) {

        switch (type) {
            case TANK: {
                playableClass = new TankPlayableClass(player, teamColor);
                break;
            }
            case ASSAULT: {
                playableClass = new AssaultPlayableClass(player, teamColor);
                break;
            }
            case ARCHER: {
                playableClass = new ArcherPlayableClass(player, teamColor);
                break;
            }
            case SWORDMAN: {
                playableClass = new SwordmanPlayableClass(player, teamColor);
                break;
            }
            case GOLEM: {
                playableClass = new GolemPlayableClass(player, teamColor);
                break;
            }
            default: {
                playableClass = new EngineerPlayableClass(player, teamColor);
                break;
            }
        }
        playableClass.reset();

        Bukkit.getPluginManager().callEvent(new PlayerSelectClassEvent(player, type));
    }

    public void pickClass(Player player, TeamColor teamColor) {
        if (playableClass == null) {
            pickClass(player, teamColor, PlayableClassType.TANK);
        } else {
            playableClass.reset();
        }
    }


    @Override
    public PlayableClassType getClassType() {
        return playableClass.getType();
    }
}
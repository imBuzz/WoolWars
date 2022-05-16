package me.buzz.woolwars.game.game.match.listener.impl;

import lombok.RequiredArgsConstructor;
import me.buzz.woolwars.api.game.arena.region.ArenaRegionType;
import me.buzz.woolwars.api.game.arena.region.Region;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.LanguageFile;
import me.buzz.woolwars.game.game.match.WoolMatch;
import me.buzz.woolwars.game.game.match.listener.MatchListener;
import me.buzz.woolwars.game.game.match.player.team.impl.WoolTeam;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

@RequiredArgsConstructor
public class BasicMatchListener implements MatchListener {

    private final WoolMatch match;

    @Override
    public void interactAtEntity(PlayerInteractAtEntityEvent event) {
        //NPC DYNAMIC
    }

    @Override
    public void damage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.CONTACT) return;
        if (event.getEntityType() != EntityType.PLAYER) return;
        Player victim = (Player) event.getEntity();

        if (event.getCause() == EntityDamageEvent.DamageCause.VOID || victim.getHealth() - event.getFinalDamage() <= 0) {
            event.setCancelled(true);
            match.handleDeath(victim, null, event.getCause());
        }
    }

    @Override
    public void damageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() != EntityType.PLAYER) return;
        if (event.getEntityType() != EntityType.PLAYER) return;

        Player victim = (Player) event.getEntity();

        if (victim.getHealth() - event.getFinalDamage() <= 0) {
            event.setCancelled(true);
            match.handleDeath(victim, (Player) event.getDamager(), event.getCause());
        }
    }

    @Override
    public void interact(PlayerInteractEvent event) {
    }

    @Override
    public void itemPickup(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void place(BlockPlaceEvent event) {
        Region centerRegion = match.getArena().getRegion(ArenaRegionType.CENTER);
        if (!centerRegion.isInRegion(event.getBlockPlaced().getLocation())) {
            event.setCancelled(true);
            return;
        }
        if (!match.getRoundHolder().isCanBreakCenter()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(WoolWars.get().getLanguage()
                    .getProperty(LanguageFile.ROUND_CANNOT_BE_CAPTURED).replace("{seconds}", match.getRoundHolder()
                            .getTasks().get("centerProtect").formatSecondsAndMillis()));
            return;
        }

        Player player = event.getPlayer();
        if (event.getBlockPlaced().getType() != Material.WOOL) return;
        match.getPlayerHolder().getMatchStats(event.getPlayer()).matchWoolPlaced++;

        WoolTeam woolTeam = match.getPlayerHolder().getMatchStats(player).getTeam();

        int totalBlocks = 0, sameTypeBlocks = 0;
        for (Block block : centerRegion.getBlocks()) {
            totalBlocks++;
            if (block.getType() != Material.WOOL) continue;

            DyeColor dyeColor = DyeColor.getByWoolData(block.getData());
            if (dyeColor == woolTeam.getTeamColor().getDC()) sameTypeBlocks++;
        }

        if (totalBlocks == sameTypeBlocks) match.getRoundHolder().endRound(woolTeam);
    }

    @Override
    public void blockBreak(BlockBreakEvent event) {
        Region centerRegion = match.getArena().getRegion(ArenaRegionType.CENTER);
        event.setCancelled(true);
        if (!centerRegion.isInRegion(event.getBlock().getLocation())) return;
        if (!match.getRoundHolder().isCanBreakCenter()) {
            event.getPlayer().sendMessage(WoolWars.get().getLanguage()
                    .getProperty(LanguageFile.ROUND_CANNOT_BE_CAPTURED).replace("{seconds}", match.getRoundHolder()
                            .getTasks().get("centerProtect").formatSecondsAndMillis()));
            return;
        }

        match.getPlayerHolder().getMatchStats(event.getPlayer()).matchBlocksBroken++;
        event.getBlock().setType(Material.AIR);
    }

    @Override
    public void dropItem(PlayerDropItemEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        event.setCancelled(true);
    }

    @Override
    public void chat(AsyncPlayerChatEvent event) {

    }

}
